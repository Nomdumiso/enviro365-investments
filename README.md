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

## Project structure (actual layout)

This repository is intentionally small for an assessment; the backend and frontend assets live at the
repository root (no nested `backend/` and `frontend/` directories). The important top-level files are:

```
/ (repo root)
  pom.xml                    Maven build (Spring Boot 3.3, Java 17)
  application.properties     Spring properties (H2 / app config)
  data.sql                   H2 seed data (3 sample investors/products)
  README.md                  Project overview, API docs, run instructions
  WithdrawalServiceTest.java Unit tests (JUnit 5 + Mockito) for business rules

  -- Java backend files (package: com.enviro.assessment.junior.nomdumiso) --
  Enviro365Application.java  Spring Boot entrypoint (main class)
  CorsConfig.java            CORS setup for local frontend
  GlobalExceptionHandler.java Centralized mapping of exceptions -> JSON (422 for business rules)
  BusinessRuleException.java / ResourceNotFoundException.java  domain exceptions
  Investor.java / Portfolio.java / Product.java  JPA entities
  ProductType.java / WithdrawalStatus.java enums
  InvestorRepository.java / ProductRepository.java / PortfolioRepository.java / WithdrawalNoticeRepository.java  Spring Data repositories
  PortfolioController.java / WithdrawalController.java  REST endpoints
  PortfolioDto.java / ProductDto.java / WithdrawalRequestDto.java / WithdrawalResponseDto.java  DTO layer
  WithdrawalService.java     Core business logic (age/balance/90% rules)
  CsvExportService.java      CSV export streaming helper

  -- Frontend (static) --
  index.html                 Static UI (withdrawal form, portfolio view)
  app.js                     Frontend UI logic
  api.js                     Fetch wrapper (calls backend at http://localhost:8080/api)
  styles.css                 Styling
```

If you prefer a two-folder layout (`backend/` and `frontend/`) for clarity, the files can be moved
without changing the build: `pom.xml` must stay at the repository root for Maven to pick it up, or the
backend's `pom.xml` can be moved into a `backend/` module and the repo converted to a multi-module build.

**How it fits together:** Enviro365Application boots a Spring Boot REST API. Controllers (PortfolioController,
WithdrawalController) accept requests and delegate to services. WithdrawalService enforces the three concrete
business rules (retirement-age check, not exceeding balance, max 90% cap), updates Product balance inside a
`@Transactional` flow, and writes a WithdrawalNotice. Repositories persist entities to an in-memory H2 DB
(seed data loaded from `data.sql`). GlobalExceptionHandler turns BusinessRuleException into 422 responses with
a clear message that the frontend (api.js → app.js) shows inline.

## How to run it
Requirements: Java 17+, Maven. The project is runnable from the repository root.

Start the backend:
```bash
# from repo root
mvn spring-boot:run
# API base: http://localhost:8080/api
# H2 console: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:enviro365db, user: sa, no password)
```

Run tests:
```bash
mvn test
```

Open the frontend (static):
- Open `index.html` directly in a browser, or serve the repo root on a static port:
```bash
python3 -m http.server 5500
# then visit http://localhost:5500/index.html
```
Frontend is configured to call the backend at `http://localhost:8080/api` and CORS is enabled for local development.

## Reproducibility / CI

I added a GitHub Actions workflow (.github/workflows/ci.yml) that runs the test suite on push and pull requests
(using JDK 17). The workflow runs `mvn test`, so it will validate the unit tests and fail the CI if anything breaks.

Optional: to make local builds independent of a system Maven installation, add the Maven Wrapper to the repo by
running locally:

```bash
# from repo root (runs locally once and commits the wrapper files)
mvn -N io.takari:maven:wrapper
```

This creates `mvnw`, `mvnw.cmd` and the `.mvn/wrapper` directory. I left this step optional because the wrapper
includes a jar binary; if you want I can add the wrapper files in a follow-up commit.

## Try asking
- Can you walk me through the exact checks and messages in WithdrawalService.validateBusinessRules (file WithdrawalService.java) and explain the ordering of rules?  
- Where is CSV export implemented (CsvExportService.java) and how would I change it to include rejected withdrawal attempts for auditing?  
- How is investor age calculated and used (see Investor.getAge() in Investor.java); are there timezone/date assumptions to be aware of for the retirement-age rule?
