# Enviro365 Investments — Withdrawal Notice System

Junior Developer Assessment (June 2026) — full-stack solution: Spring Boot backend + HTML/CSS/JS frontend.

**Author:** Nomdumiso
**Package:** `com.enviro.assessment.junior.nomdumiso`

## Scenario

Enviro365 Investments is automating its withdrawal notice process. This system lets an investor
view their portfolio, submit a withdrawal notice against a product, see their withdrawal history,
and download a CSV statement — all while enforcing the business rules below server-side.

## Business rules

1. **Retirement Annuity** withdrawals are only allowed if the investor's age is **over 65**.
2. A withdrawal may **never exceed the product's current balance**.
3. A withdrawal may **never exceed 90%** of the product's current balance.

All three are enforced in `WithdrawalService`. Violations raise a `BusinessRuleException`, which
`GlobalExceptionHandler` turns into a `422` response with a clear message, shown directly in the UI.

## Tech stack

- **Backend:** Java 17, Spring Boot 3.3 (Web, Data JPA, Validation), H2 in-memory database, Maven
- **Frontend:** Plain HTML5 / CSS3 / vanilla JavaScript (no build step required)
- **Testing:** JUnit 5 + Mockito (unit tests for the business rules)

## Project structure

```
enviro365-withdrawal-system/
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/enviro/assessment/junior/nomdumiso/
│       │   ├── Enviro365Application.java
│       │   ├── config/CorsConfig.java
│       │   ├── controller/          # REST endpoints
│       │   ├── dto/                 # Request/response shapes
│       │   ├── entity/              # JPA entities
│       │   ├── exception/           # Global error handling
│       │   ├── repository/          # Spring Data JPA repositories
│       │   └── service/             # Business logic
│       ├── main/resources/
│       │   ├── application.properties
│       │   └── data.sql             # Seed data
│       └── test/java/.../service/WithdrawalServiceTest.java
└── frontend/
    ├── index.html
    ├── css/styles.css
    └── js/
        ├── api.js                   # fetch wrapper
        └── app.js                   # UI logic
```

## Running the backend

Requires Java 17+ and Maven.

```bash
cd backend
mvn spring-boot:run
```

The API starts on **http://localhost:8080**. The H2 console is available at
`http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:enviro365db`, user `sa`, no password).

Seed data (`data.sql`) loads automatically on startup with three sample investors:

| ID | Name           | Age (approx.) | Products                                             |
|----|----------------|----------------|-------------------------------------------------------|
| 1  | Thabo Mokoena  | 71 (over 65)   | Retirement Annuity (R500,000), Unit Trust (R150,000)   |
| 2  | Lerato Dlamini | 36 (under 65)  | Retirement Annuity (R300,000), Savings Plan (R75,000)  |
| 3  | Sipho Nkosi    | 68 (over 65)   | Unit Trust (R220,000)                                  |

Investor 2 is deliberately under 65 so the retirement-age rule can be demonstrated failing.

## Running the frontend

The frontend is static — no build tooling needed. Simplest option: open `frontend/index.html`
directly in a browser, or serve it with any static server, e.g.:

```bash
cd frontend
python3 -m http.server 5500
```

Then visit `http://localhost:5500`. The frontend calls the backend at `http://localhost:8080/api`
(configured in `frontend/js/api.js`) — CORS is already enabled on the backend for local development.

## Running the tests

```bash
cd backend
mvn test
```

`WithdrawalServiceTest` covers all three business rules independently (age restriction, balance
exceeded, 90% cap exceeded), plus the "happy path" approval flow, using Mockito so no database
is required.

## API documentation

Base URL: `http://localhost:8080/api`

### `GET /investors/{investorId}/portfolio`
Returns the investor's portfolio details and products.

```json
{
  "portfolioId": 1,
  "investorId": 1,
  "investorName": "Thabo Mokoena",
  "email": "thabo.mokoena@example.com",
  "age": 71,
  "totalBalance": 650000.00,
  "products": [
    { "id": 1, "name": "Retirement Annuity - Growth Fund", "type": "RETIREMENT_ANNUITY", "balance": 500000.00 },
    { "id": 2, "name": "Unit Trust - Balanced Fund", "type": "UNIT_TRUST", "balance": 150000.00 }
  ]
}
```

### `POST /withdrawals`
Creates a withdrawal notice. Runs the three business rules server-side before committing.

Request:
```json
{ "productId": 1, "amount": 10000.00 }
```

Success (200):
```json
{
  "id": 12,
  "productId": 1,
  "productName": "Retirement Annuity - Growth Fund",
  "amount": 10000.00,
  "balanceBefore": 500000.00,
  "balanceAfter": 490000.00,
  "status": "APPROVED",
  "rejectionReason": null,
  "requestedAt": "2026-06-10T09:15:00"
}
```

Business rule failure (422):
```json
{
  "timestamp": "2026-06-10T09:15:00",
  "status": 422,
  "error": "Business Rule Violation",
  "message": "Withdrawal amount (R95000) exceeds the maximum allowed of 90% of the balance (R90000.00).",
  "details": null
}
```

### `GET /withdrawals?investorId={id}&status={APPROVED|REJECTED}&from={iso}&to={iso}`
Returns withdrawal history, filterable by investor, status, and date range. All filters optional
except `investorId`, which the frontend always supplies.

### `GET /withdrawals/export/csv?investorId={id}&status={...}&from={...}&to={...}`
Same filters as above; streams back a downloadable `withdrawal-statement.csv` file.

## Advanced requirements implemented (3+ required)

- ✅ **Global exception handling** — `GlobalExceptionHandler` maps business rule violations,
  validation errors, and not-found errors to consistent JSON responses.
- ✅ **DTO layer** — Entities are never returned directly; all requests/responses go through
  dedicated DTOs (`PortfolioDto`, `ProductDto`, `WithdrawalRequestDto`, `WithdrawalResponseDto`).
- ✅ **Input validation** — `WithdrawalRequestDto` uses Bean Validation (`@NotNull`, `@DecimalMin`)
  enforced via `@Valid` in the controller.
- ✅ **Unit tests** — `WithdrawalServiceTest` covers all three business rules with Mockito.
- ✅ **UI validation** — the withdrawal form checks for a selected product and a positive amount
  before calling the API, and surfaces backend validation/business-rule errors inline.

## AI usage disclosure

AI assistance (Claude, Anthropic) was used to help scaffold this project — generating boilerplate
(entities, repositories, DTO mapping, CSS) and drafting the README — based on my own design of the
domain model, business rules, and API shape. I reviewed, understood, and can explain every part of
this code, including the reasoning behind the JPA relationships, the transactional withdrawal flow,
and the validation/exception handling strategy, and I'm ready to discuss any design decision or
alternative approach in a follow-up interview.

## Design notes / assumptions

- A withdrawal is made against a specific **product** (not the portfolio as a whole), since real
  portfolios hold multiple products with independent balances and rules (e.g. only Retirement
  Annuities carry the age-65 restriction).
- Withdrawals are processed synchronously and atomically (`@Transactional`): the product balance
  is debited and the withdrawal notice is created in the same transaction, so a failure can't leave
  the balance and history out of sync.
- Rejected withdrawals are not persisted as failed notices — they return a `422` with a clear reason
  and never touch the balance. This was a deliberate simplification; persisting rejected attempts
  for audit purposes would be a natural next step.
- `ddl-auto=create-drop` recreates the H2 schema on every restart and reloads `data.sql`, which is
  appropriate for an in-memory demo database but would be replaced with migrations (e.g. Flyway) in
  production.
