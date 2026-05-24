# Agro Business ERP Management System

Spring Boot REST API plus Bootstrap UI scaffold for agro retailer, product, stock, order, invoice, delivery, payment, ledger, return policy, and reporting workflows.

## Stack

- Backend: Spring Boot 3, Spring Security, JWT, Spring Data JPA
- Frontend: HTML, CSS, Bootstrap, JavaScript
- Database: MySQL
- Architecture: layered/clean package structure

## Run

1. Create or update MySQL credentials in `src/main/resources/application.properties`.
2. Make sure database `agro_erp` exists, or let the JDBC URL create it.
3. Run with Maven:

```bash
mvn spring-boot:run
```

If Maven is not installed on your PATH, use the bundled copy:

```powershell
.\.tools\apache-maven-3.9.9\bin\mvn.cmd spring-boot:run
```

For a quick local run without MySQL, use the H2-backed dev profile:

```powershell
.\.tools\apache-maven-3.9.9\bin\mvn.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

## URLs

- UI: `http://localhost:9018/index.html`
- Swagger: `http://localhost:9018/swagger-ui.html`
- API base: `http://localhost:9018/api`

## Default User

- Username: `admin`
- Password: `admin123`

Change the default password and `app.jwt.secret` before production use.

## Important Files

- MySQL script: `src/main/resources/db/schema.sql`
- Architecture: `docs/ARCHITECTURE.md`
- API list and sample JSON: `docs/API.md`
- Development plan: `docs/DEVELOPMENT_PLAN.md`
- Bootstrap UI: `src/main/resources/static`

## Implemented Backend Flow

```text
Order -> Approval -> Invoice -> Delivery -> Payment -> Retailer Ledger
```

Implemented controls include JWT login, role-based method security, soft delete flags, auditing fields, credit-limit validation before order approval, negative-stock prevention before stock out, invoice paid/due transitions, and ledger posting.
