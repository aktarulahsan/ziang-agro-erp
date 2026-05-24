# Step-by-Step Development Plan

1. Configure MySQL database, application properties, and production JWT secret.
2. Finalize role permissions for every menu and API operation.
3. Complete setup modules: territories, categories, brands, units, warehouses.
4. Complete retailer module with ledger, credit limit, activity history, and due tracking.
5. Complete product/material module with image upload, batch, expiry, price history, and tax setup.
6. Complete stock module with receive, issue, adjustment, damage, return stock, warehouse stock, batch stock, and low-stock alert.
7. Complete price management with retailer-wise, territory-wise, and date-wise price validity.
8. Complete discount and offer engine with product/category/retailer/order rules and validity checks.
9. Complete order module with draft editing, approval routing, credit checking, and status history.
10. Complete invoice module with invoice print template and paid/due status transitions.
11. Complete delivery module with challan print, partial delivery, vehicle/person assignment, and confirmation.
12. Complete payment module with money receipt print, approval, invoice-wise and retailer-wise allocation.
13. Complete return module with policy validation, approval, stock update, and ledger adjustment.
14. Build reports with filterable endpoints and export to PDF/Excel.
15. Harden security: password reset, refresh tokens, audit logs, login activity, inactive user blocking.
16. Add integration tests for order, invoice, delivery, payment, return, stock, and ledger transactions.
17. Add frontend API integration, client-side validation, pagination, modals, and print pages.
18. Run UAT with real agro business scenarios and tune indexes based on slow queries.
