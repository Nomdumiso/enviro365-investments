/**
 * App state + DOM wiring for the withdrawal portal.
 * Keeps a single `state.portfolio` object as the source of truth for the
 * currently selected investor's products, refreshed after every action.
 */
const state = {
    investorId: "1",
    portfolio: null
};

const el = {
    investorSelect: document.getElementById("investorSelect"),
    refreshBtn: document.getElementById("refreshBtn"),
    portfolioSummary: document.getElementById("portfolioSummary"),
    productsTableBody: document.querySelector("#productsTable tbody"),
    productSelect: document.getElementById("productSelect"),
    amountInput: document.getElementById("amountInput"),
    balanceHint: document.getElementById("balanceHint"),
    withdrawalForm: document.getElementById("withdrawalForm"),
    formError: document.getElementById("formError"),
    historyTableBody: document.querySelector("#historyTable tbody"),
    historyEmpty: document.getElementById("historyEmpty"),
    statusFilter: document.getElementById("statusFilter"),
    downloadCsvBtn: document.getElementById("downloadCsvBtn"),
    alertBox: document.getElementById("alertBox")
};

init();

function init() {
    el.investorSelect.addEventListener("change", onInvestorChange);
    el.refreshBtn.addEventListener("click", () => loadAll());
    el.withdrawalForm.addEventListener("submit", onSubmitWithdrawal);
    el.productSelect.addEventListener("change", updateBalanceHint);
    el.statusFilter.addEventListener("change", loadHistory);
    el.downloadCsvBtn.addEventListener("click", onDownloadCsv);

    loadAll();
}

function onInvestorChange() {
    state.investorId = el.investorSelect.value;
    loadAll();
}

async function loadAll() {
    await loadPortfolio();
    await loadHistory();
}

/** Renders the Portfolio Dashboard: investor summary + products table. */
async function loadPortfolio() {
    try {
        const portfolio = await Api.getPortfolio(state.investorId);
        state.portfolio = portfolio;

        el.portfolioSummary.innerHTML = `
            <div class="stat">
                <div class="label">Investor</div>
                <div class="value">${escapeHtml(portfolio.investorName)}</div>
            </div>
            <div class="stat">
                <div class="label">Age</div>
                <div class="value">${portfolio.age}</div>
            </div>
            <div class="stat">
                <div class="label">Total Balance</div>
                <div class="value">${formatCurrency(portfolio.totalBalance)}</div>
            </div>
        `;

        el.productsTableBody.innerHTML = portfolio.products.map(p => `
            <tr>
                <td>${escapeHtml(p.name)}</td>
                <td>${formatProductType(p.type)}</td>
                <td>${formatCurrency(p.balance)}</td>
            </tr>
        `).join("");

        el.productSelect.innerHTML = portfolio.products.map(p =>
            `<option value="${p.id}" data-balance="${p.balance}" data-type="${p.type}">
                ${escapeHtml(p.name)} — ${formatCurrency(p.balance)}
             </option>`
        ).join("");

        updateBalanceHint();
    } catch (err) {
        showAlert(err.message, "error");
    }
}

function updateBalanceHint() {
    const selected = el.productSelect.selectedOptions[0];
    if (!selected) {
        el.balanceHint.textContent = "";
        return;
    }
    const balance = parseFloat(selected.dataset.balance);
    const maxWithdrawal = balance * 0.9;
    el.balanceHint.textContent =
        `Available balance: ${formatCurrency(balance)} · Max withdrawal (90%): ${formatCurrency(maxWithdrawal)}`;
}

async function onSubmitWithdrawal(event) {
    event.preventDefault();
    hideFormError();

    const productId = el.productSelect.value;
    const amount = parseFloat(el.amountInput.value);

    if (!productId) {
        return showFormError("Please select a product to withdraw from.");
    }
    if (!amount || amount <= 0) {
        return showFormError("Please enter a withdrawal amount greater than zero.");
    }

    const submitBtn = el.withdrawalForm.querySelector("button[type=submit]");
    submitBtn.disabled = true;
    submitBtn.textContent = "Submitting...";

    try {
        const result = await Api.createWithdrawal(Number(productId), amount);
        showAlert(
            `Withdrawal of ${formatCurrency(result.amount)} from "${result.productName}" was approved.`,
            "success"
        );
        el.amountInput.value = "";
        await loadAll();
    } catch (err) {
        showFormError(err.message);
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = "Submit Withdrawal";
    }
}

/** Renders the Withdrawal History table for the selected investor + status filter. */
async function loadHistory() {
    try {
        const history = await Api.getHistory(state.investorId, el.statusFilter.value);

        el.historyEmpty.classList.toggle("hidden", history.length > 0);
        el.historyTableBody.innerHTML = history.map(w => `
            <tr>
                <td>${formatDate(w.requestedAt)}</td>
                <td>${escapeHtml(w.productName)}</td>
                <td>${formatCurrency(w.amount)}</td>
                <td>${formatCurrency(w.balanceAfter)}</td>
                <td>${statusBadge(w.status)}</td>
            </tr>
        `).join("");
    } catch (err) {
        showAlert(err.message, "error");
    }
}

function onDownloadCsv() {
    const url = Api.csvDownloadUrl(state.investorId, el.statusFilter.value);
    window.open(url, "_blank");
}

/* --- Small render helpers --- */

function statusBadge(status) {
    const cls = status === "APPROVED" ? "badge-approved" : "badge-rejected";
    return `<span class="badge ${cls}">${status}</span>`;
}

function formatProductType(type) {
    return type.replace(/_/g, " ").toLowerCase().replace(/\b\w/g, c => c.toUpperCase());
}

function formatCurrency(value) {
    return new Intl.NumberFormat("en-ZA", { style: "currency", currency: "ZAR" }).format(value);
}

function formatDate(isoString) {
    return new Date(isoString).toLocaleString("en-ZA", {
        year: "numeric", month: "short", day: "2-digit", hour: "2-digit", minute: "2-digit"
    });
}

function escapeHtml(str) {
    const div = document.createElement("div");
    div.textContent = str;
    return div.innerHTML;
}

function showAlert(message, type) {
    el.alertBox.textContent = message;
    el.alertBox.className = `alert alert-${type}`;
    el.alertBox.classList.remove("hidden");
    window.scrollTo({ top: 0, behavior: "smooth" });
    setTimeout(() => el.alertBox.classList.add("hidden"), 6000);
}

function showFormError(message) {
    el.formError.textContent = message;
    el.formError.classList.remove("hidden");
}

function hideFormError() {
    el.formError.classList.add("hidden");
    el.formError.textContent = "";
}
