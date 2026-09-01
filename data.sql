-- Sample data for local development / demo purposes.
-- Investor 1 (Thabo) and Investor 3 (Sipho) are over 65 -> can withdraw from retirement annuities.
-- Investor 2 (Lerato) is under 65 -> retirement annuity withdrawals should be rejected.

INSERT INTO investors (id, first_name, last_name, email, date_of_birth) VALUES
 (1, 'Thabo', 'Mokoena', 'thabo.mokoena@example.com', '1955-03-12'),
 (2, 'Lerato', 'Dlamini', 'lerato.dlamini@example.com', '1990-07-20'),
 (3, 'Sipho', 'Nkosi', 'sipho.nkosi@example.com', '1958-01-15');

INSERT INTO portfolios (id, investor_id) VALUES
 (1, 1),
 (2, 2),
 (3, 3);

INSERT INTO products (id, portfolio_id, name, type, balance) VALUES
 (1, 1, 'Retirement Annuity - Growth Fund', 'RETIREMENT_ANNUITY', 500000.00),
 (2, 1, 'Unit Trust - Balanced Fund', 'UNIT_TRUST', 150000.00),
 (3, 2, 'Retirement Annuity - Growth Fund', 'RETIREMENT_ANNUITY', 300000.00),
 (4, 2, 'Savings Plan', 'SAVINGS_PLAN', 75000.00),
 (5, 3, 'Unit Trust - Balanced Fund', 'UNIT_TRUST', 220000.00);
