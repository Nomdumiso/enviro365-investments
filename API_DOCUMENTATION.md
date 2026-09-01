# API Documentation

## Base URL
`http://localhost:8080/api`

## Authentication
No authentication is currently required for local development.

---

## Endpoints

### 1. Get Portfolio
**Endpoint:** `GET /api/investors/{investorId}/portfolio`

**Description:** Retrieve an investor's complete portfolio including all products and withdrawal history.

**Parameters:**
- `investorId` (path parameter, required): The ID of the investor

**Response:**
```json
{
  "investorId": 1,
  "investorFirstName": "Thabo",
  "investorLastName": "Mokoena",
  "investorAge": 69,
  "investorEmail": "thabo.mokoena@example.com",
  "products": [
    {
      "id": 1,
      "name": "Retirement Annuity - Growth Fund",
      "type": "RETIREMENT_ANNUITY",
      "balance": 450000.00
    }
  ],
  "withdrawalHistory": [
    {
      "id": 1,
      "productId": 1,
      "amount": 50000.00,
      "status": "APPROVED",
      "requestDate": "2026-09-01T10:30:00"
    }
  ]
}
```

### 2. Create Withdrawal Notice
**Endpoint:** `POST /api/withdrawals`

**Request Body:**
```json
{
  "productId": 1,
  "amount": 25000.00
}
```

### 3. Get Withdrawal History
**Endpoint:** `GET /api/withdrawals/investor/{investorId}`

### 4. Export CSV
**Endpoint:** `GET /api/withdrawals/investor/{investorId}/csv-export`

---

## Business Rules

1. **Retirement Age Check:** Investor must be over 65 for Retirement Annuity withdrawals
2. **Balance Check:** Withdrawal ≤ Product balance
3. **Maximum Percentage Check:** Withdrawal ≤ 90% of product balance

All errors return HTTP 422 with clear messages.
