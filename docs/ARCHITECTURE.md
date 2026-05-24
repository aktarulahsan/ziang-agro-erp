# Agro Business ERP Management System

## Architecture

Backend follows layered/clean architecture:

- `controller`: REST endpoints and request validation.
- `service`: use-case contracts.
- `serviceImpl`: transactional business workflows.
- `repository`: Spring Data JPA persistence.
- `entity`: JPA domain entities.
- `dto`: request/response DTOs.
- `mapper`: entity to DTO mapping.
- `security`: JWT authentication and user details.
- `exception`: global exception handling.
- `response`: consistent API envelope.
- `util`: shared helpers such as document number generation.

Frontend is a Bootstrap static admin shell in `src/main/resources/static`.

## Main ERD Relationships

- `companies` provide the company/tenant base for users.
- `users` belong to one company and have many-to-many `roles` through `user_roles`.
- `retailers` belongs to `territories`.
- `products` belongs to `categories`, `brands`, and `units`.
- `stock_transactions` belongs to `products` and `warehouses`.
- `orders` belongs to `retailers`; `order_items` belongs to `orders` and `products`.
- `invoices` belongs to one `orders` row and one `retailers` row; `invoice_items` mirrors order lines.
- `deliveries` belongs to `invoices` and `retailers`; `delivery_items` belongs to `products`.
- `payments` belongs to `retailers` and optionally `invoices`.
- `retailer_ledger` stores all financial movements for a retailer with a running balance.
- `order_returns` belongs to `invoices`; approved returns post stock-in and ledger credit adjustment.

## Workflow

1. Sales user or retailer creates an order.
2. Manager approves order after credit-limit validation.
3. Accounts generates invoice from approved order.
4. Store/delivery user creates delivery from invoice.
5. Delivery posts `SALES_OUT` stock transactions and prevents negative stock.
6. Accounts receives full or partial payment.
7. Payment updates invoice status and posts retailer ledger credit.
8. Return approval should post `RETURN_IN` stock and `RETURN_ADJUSTMENT` ledger credit.

## Retailer Ledger Logic

- Debit increases retailer due.
- Credit decreases retailer due.
- Opening balance: debit.
- Invoice: debit by invoice net amount.
- Payment: credit by received amount.
- Return adjustment: credit by approved return value.
- Discount adjustment: credit by approved discount value.

Running balance formula:

```text
newBalance = previousRetailerDue + debitAmount - creditAmount
```

The application stores the current due on `retailers.current_due_balance` and also writes immutable ledger rows for history.

## Default Login

- Username: `admin`
- Password: `admin123`

Change this immediately after first run.
