/**
 * Thin wrapper around the Enviro365 Withdrawal API.
 * Keeping fetch calls in one place makes it easy to change the base URL
 * or swap in real auth headers later without touching app.js.
 */
const API_BASE_URL = "http://localhost:8080/api";

const Api = {

    async getPortfolio(investorId) {
        return handle(await fetch(`${API_BASE_URL}/investors/${investorId}/portfolio`));
    },

    async createWithdrawal(productId, amount) {
        return handle(await fetch(`${API_BASE_URL}/withdrawals`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ productId, amount })
        }));
    },

    async getHistory(investorId, status) {
        const params = new URLSearchParams({ investorId });
        if (status) params.append("status", status);
        return handle(await fetch(`${API_BASE_URL}/withdrawals?${params.toString()}`));
    },

    csvDownloadUrl(investorId, status) {
        const params = new URLSearchParams({ investorId });
        if (status) params.append("status", status);
        return `${API_BASE_URL}/withdrawals/export/csv?${params.toString()}`;
    }
};

/**
 * Normalises fetch's error handling: any non-2xx response is parsed for the
 * ApiError body from the backend's GlobalExceptionHandler and re-thrown as
 * a JS Error with that message, so the UI can show real feedback instead of
 * a generic "fetch failed".
 */
async function handle(response) {
    if (response.ok) {
        return response.status === 204 ? null : response.json();
    }
    let message = `Request failed (${response.status})`;
    try {
        const body = await response.json();
        if (body.message) message = body.message;
        if (body.details && body.details.length) message += " — " + body.details.join(", ");
    } catch (e) {
        // response wasn't JSON; fall back to the generic message above
    }
    throw new Error(message);
}
