# REST API List

Base URL: `http://localhost:8080`

## Authentication

- `POST /api/auth/login`

Request:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Response:

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "jwt-token",
    "tokenType": "Bearer",
    "username": "admin",
    "companyId": 1,
    "companyName": "Agro Business Ltd.",
    "roles": ["ROLE_SUPER_ADMIN"]
  }
}
```

Use `Authorization: Bearer <token>` for protected APIs.

## Setup

- `GET /api/setup/companies`
- `POST /api/setup/companies`
- `GET /api/setup/categories`
- `POST /api/setup/categories`
- `GET /api/setup/brands`
- `POST /api/setup/brands`
- `GET /api/setup/units`
- `POST /api/setup/units`
- `GET /api/setup/warehouses`
- `POST /api/setup/warehouses`
- `GET /api/setup/territories`
- `POST /api/setup/territories`

## User Management

- `GET /api/users`
- `POST /api/users`
- `PATCH /api/users/{id}/status?active=true`

Sample user:

```json
{
  "username": "sales01",
  "email": "sales01@example.com",
  "fullName": "Sales Officer 01",
  "password": "StrongPass123",
  "companyId": 1,
  "roleIds": [4],
  "active": true
}
```

## Retailers

- `GET /api/retailers?q=&page=0&size=20`
- `GET /api/retailers/{id}`
- `POST /api/retailers`
- `PUT /api/retailers/{id}`
- `DELETE /api/retailers/{id}`

Sample create:

```json
{
  "retailerCode": "RET-001",
  "retailerName": "Green Agro Retail",
  "ownerName": "Rahim Uddin",
  "mobileNumber": "01700000000",
  "email": "retailer@example.com",
  "address": "Market Road, Dhaka",
  "territoryId": 1,
  "marketName": "Farmgate",
  "creditLimit": 100000,
  "openingBalance": 5000,
  "active": true
}
```

## Products

- `GET /api/products?q=&page=0&size=20`
- `GET /api/products/{id}`
- `POST /api/products`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`

Sample create:

```json
{
  "productCode": "FERT-001",
  "productName": "Organic Fertilizer 25KG",
  "categoryId": 1,
  "brandId": 1,
  "unitId": 1,
  "packSize": "25 KG Bag",
  "purchasePrice": 850,
  "salesPrice": 1000,
  "retailerPrice": 950,
  "vatPercent": 5,
  "discountPercent": 2,
  "batchNumber": "B-2026-01",
  "expiryDate": "2027-12-31",
  "reorderLevel": 50,
  "active": true
}
```

## Stock

- `POST /api/stock/transactions`
- `GET /api/stock/balance?productId=1&warehouseId=1`

Sample opening stock:

```json
{
  "productId": 1,
  "warehouseId": 1,
  "transactionType": "OPENING",
  "quantity": 500,
  "batchNumber": "B-2026-01",
  "expiryDate": "2027-12-31",
  "remarks": "Initial stock"
}
```

## Order to Payment Workflow

- `POST /api/orders`
- `POST /api/orders/{id}/approve`
- `POST /api/workflow/orders/{orderId}/invoice`
- `POST /api/workflow/invoices/{invoiceId}/delivery`
- `POST /api/workflow/payments`
- `GET /api/ledger/retailers/{retailerId}`

Sample order:

```json
{
  "retailerId": 1,
  "items": [
    { "productId": 1, "quantity": 10 }
  ]
}
```

Sample delivery:

```json
{
  "warehouseId": 1,
  "deliveryAddress": "Retailer shop address",
  "vehicleDetails": "Dhaka Metro TA-1234",
  "deliveryPerson": "Kamal"
}
```

Sample payment:

```json
{
  "retailerId": 1,
  "invoiceId": 1,
  "paymentMethod": "CASH",
  "amount": 5000,
  "referenceNumber": "CASH-001",
  "remarks": "Partial payment"
}
```

## Dashboard and Reports

- `GET /api/dashboard/summary`
- `GET /api/policies/returns`

Planned report endpoints:

- `GET /api/reports/daily-orders`
- `GET /api/reports/daily-invoices`
- `GET /api/reports/daily-deliveries`
- `GET /api/reports/daily-collection`
- `GET /api/reports/retailer-sales`
- `GET /api/reports/retailer-due`
- `GET /api/reports/retailer-ledger`
- `GET /api/reports/product-sales`
- `GET /api/reports/stock`
- `GET /api/reports/low-stock`
- `GET /api/reports/batch-stock`
- `GET /api/reports/expiry-products`
- `GET /api/reports/payments`
- `GET /api/reports/returns`
- `GET /api/reports/discount-offers`
- `GET /api/reports/profit-loss`
