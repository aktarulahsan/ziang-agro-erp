package com.agroerp.serviceImpl;

import com.agroerp.dto.ReportDtos.ReportColumnDto;
import com.agroerp.dto.ReportDtos.ReportMetricDto;
import com.agroerp.dto.ReportDtos.ReportResponseDto;
import com.agroerp.dto.ReportDtos.ReportTableDto;
import com.agroerp.entity.*;
import com.agroerp.enums.DeliveryStatus;
import com.agroerp.enums.InvoiceStatus;
import com.agroerp.enums.LedgerType;
import com.agroerp.enums.OrderStatus;
import com.agroerp.enums.StockTransactionType;
import com.agroerp.repository.*;
import com.agroerp.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {
    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;
    private final RetailerRepository retailerRepository;
    private final RetailerLedgerRepository retailerLedgerRepository;
    private final ProductRepository productRepository;
    private final StockTransactionRepository stockTransactionRepository;

    public ReportServiceImpl(OrderRepository orderRepository, InvoiceRepository invoiceRepository,
                             DeliveryRepository deliveryRepository, PaymentRepository paymentRepository,
                             RetailerRepository retailerRepository, RetailerLedgerRepository retailerLedgerRepository,
                             ProductRepository productRepository,
                             StockTransactionRepository stockTransactionRepository) {
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.deliveryRepository = deliveryRepository;
        this.paymentRepository = paymentRepository;
        this.retailerRepository = retailerRepository;
        this.retailerLedgerRepository = retailerLedgerRepository;
        this.productRepository = productRepository;
        this.stockTransactionRepository = stockTransactionRepository;
    }

    @Override
    public ReportResponseDto overview(LocalDate fromDate, LocalDate toDate) {
        List<Order> orders = orderRepository.reportOrders(fromDate, toDate);
        List<Invoice> invoices = invoiceRepository.reportInvoices(fromDate, toDate);
        List<Delivery> deliveries = deliveryRepository.reportDeliveries(fromDate, toDate);
        List<Payment> payments = paymentRepository.reportPayments(fromDate, toDate);

        List<Invoice> activeInvoices = invoices.stream()
                .filter(invoice -> invoice.getStatus() != InvoiceStatus.CANCELLED)
                .toList();
        BigDecimal invoiceValue = sumInvoices(activeInvoices);
        BigDecimal collection = sumPayments(payments);
        BigDecimal netReceivable = invoiceValue.subtract(collection).max(BigDecimal.ZERO);
        BigDecimal due = activeInvoices.stream().map(Invoice::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(row("section", "Sales Orders", "count", orders.size(), "amount", BigDecimal.ZERO));
        rows.add(row("section", "Invoices", "count", activeInvoices.size(), "amount", invoiceValue));
        rows.add(row("section", "Deliveries", "count", deliveries.size(), "amount", BigDecimal.ZERO));
        rows.add(row("section", "Collections", "count", payments.size(), "amount", collection.negate()));

        return report("overview", "Business Overview", fromDate, toDate,
                List.of(metric("Orders", orders.size(), "number"), metric("Invoices", invoices.size(), "number"),
                        metric("Deliveries", deliveries.size(), "number"), metric("Collection", collection, "money"),
                        metric("Invoice Due", due, "money"), metric("Net Receivable", netReceivable, "money")),
                List.of(table("Period Summary", List.of(col("section", "Report", "text"),
                        col("count", "Count", "number"), col("amount", "Amount", "money")), rows,
                        row("count", orders.size() + invoices.size() + deliveries.size() + payments.size(),
                                "amount", netReceivable))));
    }

    @Override
    public ReportResponseDto orders(LocalDate fromDate, LocalDate toDate) {
        List<Order> orders = orderRepository.reportOrders(fromDate, toDate);
        List<Map<String, Object>> rows = orders.stream().map(order -> row(
                "orderNumber", order.getOrderNumber(),
                "orderDate", order.getOrderDate(),
                "retailerName", order.getRetailer().getRetailerName(),
                "status", order.getStatus(),
                "grossAmount", order.getGrossAmount(),
                "discountAmount", order.getDiscountAmount(),
                "vatAmount", order.getVatAmount(),
                "netAmount", order.getNetAmount()
        )).toList();

        return report("orders", "Order Report", fromDate, toDate,
                List.of(metric("Orders", orders.size(), "number"), metric("Net Value", sumOrders(orders), "money"),
                        metric("Discount", sum(orders, Order::getDiscountAmount), "money")),
                List.of(table("Orders", List.of(col("orderNumber", "Order No", "text"), col("orderDate", "Date", "date"),
                        col("retailerName", "Retailer", "text"), col("status", "Status", "text"),
                        col("grossAmount", "Gross", "money"), col("discountAmount", "Discount", "money"),
                        col("vatAmount", "VAT", "money"), col("netAmount", "Net", "money")), rows,
                        row("grossAmount", sum(orders, Order::getGrossAmount), "discountAmount", sum(orders, Order::getDiscountAmount),
                                "vatAmount", sum(orders, Order::getVatAmount), "netAmount", sumOrders(orders)))));
    }

    @Override
    public ReportResponseDto invoices(LocalDate fromDate, LocalDate toDate) {
        List<Invoice> invoices = invoiceRepository.reportInvoices(fromDate, toDate);
        List<Map<String, Object>> rows = invoices.stream().map(invoice -> row(
                "invoiceNumber", invoice.getInvoiceNumber(),
                "invoiceDate", invoice.getInvoiceDate(),
                "retailerName", invoice.getRetailer().getRetailerName(),
                "orderNumber", invoice.getOrder() == null ? "" : invoice.getOrder().getOrderNumber(),
                "status", invoice.getStatus(),
                "netAmount", invoice.getNetAmount(),
                "paidAmount", invoice.getPaidAmount(),
                "dueAmount", invoice.getDueAmount()
        )).toList();

        return report("invoices", "Invoice Report", fromDate, toDate,
                List.of(metric("Invoices", invoices.size(), "number"), metric("Invoice Value", sumInvoices(invoices), "money"),
                        metric("Paid", sum(invoices, Invoice::getPaidAmount), "money"), metric("Due", sum(invoices, Invoice::getDueAmount), "money")),
                List.of(table("Invoices", List.of(col("invoiceNumber", "Invoice No", "text"), col("invoiceDate", "Date", "date"),
                        col("retailerName", "Retailer", "text"), col("orderNumber", "Order No", "text"),
                        col("status", "Status", "text"), col("netAmount", "Net", "money"),
                        col("paidAmount", "Paid", "money"), col("dueAmount", "Due", "money")), rows,
                        row("netAmount", sumInvoices(invoices), "paidAmount", sum(invoices, Invoice::getPaidAmount),
                                "dueAmount", sum(invoices, Invoice::getDueAmount)))));
    }

    @Override
    public ReportResponseDto deliveries(LocalDate fromDate, LocalDate toDate) {
        List<Delivery> deliveries = deliveryRepository.reportDeliveries(fromDate, toDate);
        List<Map<String, Object>> rows = deliveries.stream().map(delivery -> row(
                "deliveryNumber", delivery.getDeliveryNumber(),
                "deliveryDate", delivery.getDeliveryDate(),
                "retailerName", delivery.getRetailer().getRetailerName(),
                "invoiceNumber", delivery.getInvoice().getInvoiceNumber(),
                "status", delivery.getStatus(),
                "deliveryPerson", delivery.getDeliveryPerson(),
                "routeName", delivery.getRouteName(),
                "itemCount", delivery.getItems().size(),
                "transportCost", delivery.getTransportCost()
        )).toList();

        return report("deliveries", "Delivery Report", fromDate, toDate,
                List.of(metric("Deliveries", deliveries.size(), "number"),
                        metric("Delivered", deliveries.stream().filter(d -> d.getStatus() == DeliveryStatus.DELIVERED).count(), "number"),
                        metric("Transport Cost", sumDeliveryCost(deliveries), "money")),
                List.of(table("Deliveries", List.of(col("deliveryNumber", "Delivery No", "text"), col("deliveryDate", "Date", "date"),
                        col("retailerName", "Retailer", "text"), col("invoiceNumber", "Invoice", "text"),
                        col("status", "Status", "text"), col("deliveryPerson", "Person", "text"),
                        col("routeName", "Route", "text"), col("itemCount", "Items", "number"),
                        col("transportCost", "Transport", "money")), rows,
                        row("itemCount", deliveries.stream().mapToInt(d -> d.getItems().size()).sum(),
                                "transportCost", sumDeliveryCost(deliveries)))));
    }

    @Override
    public ReportResponseDto payments(LocalDate fromDate, LocalDate toDate) {
        List<Payment> payments = paymentRepository.reportPayments(fromDate, toDate);
        List<Map<String, Object>> rows = payments.stream().map(payment -> row(
                "receiptNumber", payment.getReceiptNumber(),
                "paymentDate", payment.getPaymentDate(),
                "retailerName", payment.getRetailer().getRetailerName(),
                "invoiceNumber", payment.getInvoice() == null ? "" : payment.getInvoice().getInvoiceNumber(),
                "paymentMethod", payment.getPaymentMethod(),
                "referenceNumber", payment.getReferenceNumber(),
                "approved", payment.isApproved() ? "Yes" : "No",
                "amount", payment.getAmount()
        )).toList();

        return report("payments", "Collection Report", fromDate, toDate,
                List.of(metric("Receipts", payments.size(), "number"), metric("Collection", sumPayments(payments), "money")),
                List.of(table("Payments", List.of(col("receiptNumber", "Receipt No", "text"), col("paymentDate", "Date", "date"),
                        col("retailerName", "Retailer", "text"), col("invoiceNumber", "Invoice", "text"),
                        col("paymentMethod", "Method", "text"), col("referenceNumber", "Reference", "text"),
                        col("approved", "Approved", "text"), col("amount", "Amount", "money")), rows,
                        row("amount", sumPayments(payments)))));
    }

    @Override
    public ReportResponseDto retailerSales(LocalDate fromDate, LocalDate toDate) {
        List<Invoice> invoices = invoiceRepository.reportInvoices(fromDate, toDate);
        Map<Long, RetailerSales> sales = new LinkedHashMap<>();
        for (Invoice invoice : invoices) {
            Retailer retailer = invoice.getRetailer();
            sales.computeIfAbsent(retailer.getId(), id -> new RetailerSales(retailer))
                    .add(invoice.getNetAmount(), invoice.getPaidAmount(), invoice.getDueAmount());
        }
        List<Map<String, Object>> rows = sales.values().stream().map(item -> row(
                "retailerCode", item.retailer.getRetailerCode(),
                "retailerName", item.retailer.getRetailerName(),
                "mobileNumber", item.retailer.getMobileNumber(),
                "invoiceCount", item.invoiceCount,
                "salesAmount", item.salesAmount,
                "paidAmount", item.paidAmount,
                "dueAmount", item.dueAmount
        )).toList();

        return report("retailer-sales", "Retailer Sales Report", fromDate, toDate,
                List.of(metric("Retailers", rows.size(), "number"), metric("Sales", sales.values().stream().map(s -> s.salesAmount).reduce(BigDecimal.ZERO, BigDecimal::add), "money")),
                List.of(table("Retailer Sales", List.of(col("retailerCode", "Code", "text"), col("retailerName", "Retailer", "text"),
                        col("mobileNumber", "Mobile", "text"), col("invoiceCount", "Invoices", "number"),
                        col("salesAmount", "Sales", "money"), col("paidAmount", "Paid", "money"),
                        col("dueAmount", "Due", "money")), rows,
                        row("invoiceCount", sales.values().stream().mapToLong(s -> s.invoiceCount).sum(),
                                "salesAmount", sales.values().stream().map(s -> s.salesAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
                                "paidAmount", sales.values().stream().map(s -> s.paidAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
                                "dueAmount", sales.values().stream().map(s -> s.dueAmount).reduce(BigDecimal.ZERO, BigDecimal::add)))));
    }

    @Override
    public ReportResponseDto retailerDue() {
        List<Retailer> retailers = retailerRepository.findByDeletedFalseOrderByRetailerNameAsc();
        List<Map<String, Object>> rows = retailers.stream().map(retailer -> row(
                "retailerCode", retailer.getRetailerCode(),
                "retailerName", retailer.getRetailerName(),
                "mobileNumber", retailer.getMobileNumber(),
                "territoryName", retailer.getTerritory() == null ? "" : retailer.getTerritory().getName(),
                "creditLimit", retailer.getCreditLimit(),
                "openingBalance", retailer.getOpeningBalance(),
                "currentDueBalance", retailer.getCurrentDueBalance(),
                "availableCredit", safe(retailer.getCreditLimit()).subtract(safe(retailer.getCurrentDueBalance()))
        )).toList();

        BigDecimal totalDue = retailers.stream().map(Retailer::getCurrentDueBalance).map(this::safe).reduce(BigDecimal.ZERO, BigDecimal::add);
        return report("retailer-due", "Retailer Due Report", LocalDate.now(), LocalDate.now(),
                List.of(metric("Retailers", retailers.size(), "number"), metric("Total Due", totalDue, "money")),
                List.of(table("Retailer Due", List.of(col("retailerCode", "Code", "text"), col("retailerName", "Retailer", "text"),
                        col("mobileNumber", "Mobile", "text"), col("territoryName", "Territory", "text"),
                        col("creditLimit", "Credit Limit", "money"), col("openingBalance", "Opening", "money"),
                        col("currentDueBalance", "Current Due", "money"), col("availableCredit", "Available Credit", "money")), rows,
                        row("creditLimit", retailers.stream().map(Retailer::getCreditLimit).map(this::safe).reduce(BigDecimal.ZERO, BigDecimal::add),
                                "openingBalance", retailers.stream().map(Retailer::getOpeningBalance).map(this::safe).reduce(BigDecimal.ZERO, BigDecimal::add),
                                "currentDueBalance", totalDue))));
    }

    @Override
    public ReportResponseDto productSales(LocalDate fromDate, LocalDate toDate) {
        List<Invoice> invoices = invoiceRepository.reportInvoicesWithItems(fromDate, toDate);
        Map<Long, ProductSales> sales = new LinkedHashMap<>();
        for (Invoice invoice : invoices) {
            if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
                continue;
            }
            for (InvoiceItem item : invoice.getItems()) {
                Product product = item.getProduct();
                sales.computeIfAbsent(product.getId(), id -> new ProductSales(product))
                        .add(item.getQuantity(), item.getLineTotal());
            }
        }
        List<Map<String, Object>> rows = sales.values().stream().map(item -> row(
                "productCode", item.product.getProductCode(),
                "productName", item.product.getProductName(),
                "categoryName", item.product.getCategory() == null ? "" : item.product.getCategory().getName(),
                "quantity", item.quantity,
                "salesAmount", item.salesAmount,
                "purchaseCost", item.purchaseCost(),
                "estimatedProfit", item.estimatedProfit(),
                "averageRate", item.averageRate()
        )).toList();

        BigDecimal totalSales = sales.values().stream().map(s -> s.salesAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalProfit = sales.values().stream().map(ProductSales::estimatedProfit).reduce(BigDecimal.ZERO, BigDecimal::add);
        return report("product-sales", "Product Sales Report", fromDate, toDate,
                List.of(metric("Products", rows.size(), "number"), metric("Sales", totalSales, "money"), metric("Estimated Profit", totalProfit, "money")),
                List.of(table("Product Sales", List.of(col("productCode", "Code", "text"), col("productName", "Product", "text"),
                        col("categoryName", "Category", "text"), col("quantity", "Qty", "quantity"),
                        col("averageRate", "Avg Rate", "money"), col("salesAmount", "Sales", "money"),
                        col("purchaseCost", "Cost", "money"), col("estimatedProfit", "Profit", "money")), rows,
                        row("quantity", sales.values().stream().map(s -> s.quantity).reduce(BigDecimal.ZERO, BigDecimal::add),
                                "salesAmount", totalSales,
                                "purchaseCost", sales.values().stream().map(ProductSales::purchaseCost).reduce(BigDecimal.ZERO, BigDecimal::add),
                                "estimatedProfit", totalProfit))));
    }

    @Override
    public ReportResponseDto retailerLedger(Long retailerId, LocalDate fromDate, LocalDate toDate) {
        List<RetailerLedger> ledger = retailerLedgerRepository.reportLedger(retailerId, fromDate, toDate);
        List<Map<String, Object>> rows = ledger.stream().map(entry -> row(
                "transactionDate", entry.getTransactionDate(),
                "retailerCode", entry.getRetailer().getRetailerCode(),
                "retailerName", entry.getRetailer().getRetailerName(),
                "ledgerType", entry.getLedgerType(),
                "reference", (entry.getReferenceType() == null ? "" : entry.getReferenceType()) + (entry.getReferenceId() == null ? "" : " #" + entry.getReferenceId()),
                "narration", entry.getNarration(),
                "debitAmount", entry.getDebitAmount(),
                "creditAmount", entry.getCreditAmount(),
                "runningBalance", entry.getRunningBalance()
        )).toList();

        return report("retailer-ledger", "Retailer Ledger Report", fromDate, toDate,
                List.of(metric("Entries", ledger.size(), "number"), metric("Debit", sum(ledger, RetailerLedger::getDebitAmount), "money"),
                        metric("Credit", sum(ledger, RetailerLedger::getCreditAmount), "money")),
                List.of(table("Retailer Ledger", List.of(col("transactionDate", "Date", "date"),
                        col("retailerCode", "Code", "text"), col("retailerName", "Retailer", "text"),
                        col("ledgerType", "Type", "text"), col("reference", "Reference", "text"),
                        col("narration", "Narration", "text"), col("debitAmount", "Debit", "money"),
                        col("creditAmount", "Credit", "money"), col("runningBalance", "Balance", "money")), rows,
                        row("debitAmount", sum(ledger, RetailerLedger::getDebitAmount),
                                "creditAmount", sum(ledger, RetailerLedger::getCreditAmount)))));
    }

    @Override
    public ReportResponseDto stock() {
        List<ProductStock> products = productRepository.findByDeletedFalseOrderByProductNameAsc().stream()
                .map(this::productStock)
                .toList();
        List<Map<String, Object>> rows = products.stream().map(this::stockRow).toList();
        BigDecimal stockValue = products.stream().map(ProductStock::inventoryValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        return report("stock", "Stock Report", LocalDate.now(), LocalDate.now(),
                List.of(metric("Products", products.size(), "number"), metric("Stock Value", stockValue, "money")),
                List.of(stockTable("Stock", rows, row("stockQuantity", products.stream().map(p -> p.stockQuantity).reduce(BigDecimal.ZERO, BigDecimal::add),
                        "inventoryValue", stockValue))));
    }

    @Override
    public ReportResponseDto lowStock() {
        List<ProductStock> products = productRepository.findByDeletedFalseOrderByProductNameAsc().stream()
                .map(this::productStock)
                .filter(product -> product.stockQuantity.compareTo(safe(product.product.getReorderLevel())) <= 0)
                .toList();
        List<Map<String, Object>> rows = products.stream().map(this::stockRow).toList();
        return report("low-stock", "Low Stock Report", LocalDate.now(), LocalDate.now(),
                List.of(metric("Low Stock Items", products.size(), "number")),
                List.of(stockTable("Low Stock", rows, row("stockQuantity", products.stream().map(p -> p.stockQuantity).reduce(BigDecimal.ZERO, BigDecimal::add),
                        "inventoryValue", products.stream().map(ProductStock::inventoryValue).reduce(BigDecimal.ZERO, BigDecimal::add)))));
    }

    @Override
    public ReportResponseDto batchStock() {
        Map<String, BatchStock> batches = new LinkedHashMap<>();
        for (StockTransaction tx : stockTransactionRepository.reportBatchMovements()) {
            String key = tx.getProduct().getId() + "|" + tx.getWarehouse().getId() + "|" + nullSafe(tx.getBatchNumber()) + "|" + nullSafe(tx.getExpiryDate());
            batches.computeIfAbsent(key, ignored -> new BatchStock(tx)).add(signedQuantity(tx));
        }
        List<Map<String, Object>> rows = batches.values().stream().map(batch -> row(
                "productCode", batch.product.getProductCode(),
                "productName", batch.product.getProductName(),
                "warehouseName", batch.warehouse.getName(),
                "batchNumber", batch.batchNumber,
                "expiryDate", batch.expiryDate,
                "stockQuantity", batch.stockQuantity
        )).toList();
        return report("batch-stock", "Batch Stock Report", LocalDate.now(), LocalDate.now(),
                List.of(metric("Batches", rows.size(), "number"), metric("Quantity", batches.values().stream().map(b -> b.stockQuantity).reduce(BigDecimal.ZERO, BigDecimal::add), "quantity")),
                List.of(table("Batch Stock", List.of(col("productCode", "Code", "text"), col("productName", "Product", "text"),
                        col("warehouseName", "Warehouse", "text"), col("batchNumber", "Batch", "text"),
                        col("expiryDate", "Expiry", "date"), col("stockQuantity", "Stock", "quantity")), rows,
                        row("stockQuantity", batches.values().stream().map(b -> b.stockQuantity).reduce(BigDecimal.ZERO, BigDecimal::add)))));
    }

    @Override
    public ReportResponseDto expiryProducts(LocalDate fromDate, LocalDate toDate) {
        List<Product> products = productRepository.findByDeletedFalseAndExpiryDateBetweenOrderByExpiryDateAsc(fromDate, toDate);
        List<Map<String, Object>> rows = products.stream().map(product -> row(
                "productCode", product.getProductCode(),
                "productName", product.getProductName(),
                "batchNumber", product.getBatchNumber(),
                "expiryDate", product.getExpiryDate(),
                "stockQuantity", product.getTotalStockQuantity(),
                "inventoryValue", product.getCurrentInventoryValue()
        )).toList();
        return report("expiry-products", "Expiry Product Report", fromDate, toDate,
                List.of(metric("Expiring Items", products.size(), "number")),
                List.of(table("Expiry Products", List.of(col("productCode", "Code", "text"), col("productName", "Product", "text"),
                        col("batchNumber", "Batch", "text"), col("expiryDate", "Expiry", "date"),
                        col("stockQuantity", "Stock", "quantity"), col("inventoryValue", "Value", "money")), rows,
                        row("stockQuantity", products.stream().map(Product::getTotalStockQuantity).map(this::safe).reduce(BigDecimal.ZERO, BigDecimal::add),
                                "inventoryValue", products.stream().map(Product::getCurrentInventoryValue).map(this::safe).reduce(BigDecimal.ZERO, BigDecimal::add)))));
    }

    @Override
    public ReportResponseDto discountOffers() {
        List<Product> products = productRepository.findByDeletedFalseOrderByProductNameAsc().stream()
                .filter(product -> safe(product.getDiscountPercent()).compareTo(BigDecimal.ZERO) > 0)
                .toList();
        List<Map<String, Object>> rows = products.stream().map(product -> row(
                "productCode", product.getProductCode(),
                "productName", product.getProductName(),
                "salesPrice", product.getSalesPrice(),
                "retailerPrice", product.getRetailerPrice(),
                "discountPercent", product.getDiscountPercent(),
                "vatPercent", product.getVatPercent(),
                "currentPrice", product.getCurrentPrice()
        )).toList();

        return report("discount-offers", "Discount Offer Report", LocalDate.now(), LocalDate.now(),
                List.of(metric("Discounted Products", products.size(), "number")),
                List.of(table("Discount Offers", List.of(col("productCode", "Code", "text"),
                        col("productName", "Product", "text"), col("salesPrice", "Sales Price", "money"),
                        col("retailerPrice", "Retailer Price", "money"), col("discountPercent", "Discount %", "number"),
                        col("vatPercent", "VAT %", "number"), col("currentPrice", "Current Price", "money")), rows,
                        row("discountPercent", products.stream().map(Product::getDiscountPercent).map(this::safe).reduce(BigDecimal.ZERO, BigDecimal::add)))));
    }

    @Override
    public ReportResponseDto returns(LocalDate fromDate, LocalDate toDate) {
        List<RetailerLedger> returns = retailerLedgerRepository.reportLedger(null, fromDate, toDate).stream()
                .filter(entry -> entry.getLedgerType() == LedgerType.RETURN_ADJUSTMENT)
                .toList();
        List<Map<String, Object>> rows = returns.stream().map(entry -> row(
                "date", entry.getTransactionDate(),
                "reference", (entry.getReferenceType() == null ? "" : entry.getReferenceType()) + (entry.getReferenceId() == null ? "" : " #" + entry.getReferenceId()),
                "retailerName", entry.getRetailer().getRetailerName(),
                "narration", entry.getNarration(),
                "amount", entry.getCreditAmount()
        )).toList();
        BigDecimal total = returns.stream().map(RetailerLedger::getCreditAmount).map(this::safe).reduce(BigDecimal.ZERO, BigDecimal::add);
        return report("returns", "Returns Report", fromDate, toDate,
                List.of(metric("Returns", returns.size(), "number"), metric("Return Value", total, "money")),
                List.of(table("Returns", List.of(col("date", "Date", "date"), col("reference", "Reference", "text"),
                        col("retailerName", "Retailer", "text"), col("narration", "Narration", "text"),
                        col("amount", "Amount", "money")), rows, row("amount", total))));
    }

    @Override
    public ReportResponseDto profitLoss(LocalDate fromDate, LocalDate toDate) {
        List<Invoice> invoices = invoiceRepository.reportInvoicesWithItems(fromDate, toDate);
        List<Delivery> deliveries = deliveryRepository.reportDeliveries(fromDate, toDate);
        BigDecimal revenue = BigDecimal.ZERO;
        BigDecimal cost = BigDecimal.ZERO;
        for (Invoice invoice : invoices) {
            if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
                continue;
            }
            revenue = revenue.add(safe(invoice.getNetAmount()));
            for (InvoiceItem item : invoice.getItems()) {
                cost = cost.add(safe(item.getProduct().getPurchasePrice()).multiply(safe(item.getQuantity())));
            }
        }
        BigDecimal deliveryCost = sumDeliveryCost(deliveries);
        BigDecimal grossProfit = revenue.subtract(cost);
        BigDecimal netProfit = grossProfit.subtract(deliveryCost);
        List<Map<String, Object>> rows = List.of(
                row("line", "Sales Revenue", "amount", revenue),
                row("line", "Cost of Goods Sold", "amount", cost.negate()),
                row("line", "Gross Profit", "amount", grossProfit),
                row("line", "Delivery Expense", "amount", deliveryCost.negate()),
                row("line", "Net Profit", "amount", netProfit)
        );

        return report("profit-loss", "Profit and Loss Report", fromDate, toDate,
                List.of(metric("Revenue", revenue, "money"), metric("Gross Profit", grossProfit, "money"),
                        metric("Net Profit", netProfit, "money")),
                List.of(table("Profit and Loss", List.of(col("line", "Line", "text"), col("amount", "Amount", "money")), rows,
                        row("amount", netProfit))));
    }

    private ReportResponseDto report(String key, String title, LocalDate fromDate, LocalDate toDate,
                                     List<ReportMetricDto> metrics, List<ReportTableDto> tables) {
        return new ReportResponseDto(key, title, periodLabel(fromDate, toDate), fromDate, toDate,
                Instant.now(), metrics, tables);
    }

    private ReportTableDto table(String title, List<ReportColumnDto> columns, List<Map<String, Object>> rows,
                                 Map<String, Object> totals) {
        return new ReportTableDto(title, columns, rows, totals);
    }

    private ReportTableDto stockTable(String title, List<Map<String, Object>> rows, Map<String, Object> totals) {
        return table(title, List.of(col("productCode", "Code", "text"), col("productName", "Product", "text"),
                col("categoryName", "Category", "text"), col("batchNumber", "Batch", "text"),
                col("expiryDate", "Expiry", "date"), col("reorderLevel", "Reorder", "quantity"),
                col("stockQuantity", "Stock", "quantity"), col("inventoryValue", "Value", "money"),
                col("stockStatus", "Status", "text")), rows, totals);
    }

    private ProductStock productStock(Product product) {
        return new ProductStock(product, stockTransactionRepository.currentStockAllWarehouses(product.getId()));
    }

    private Map<String, Object> stockRow(ProductStock productStock) {
        Product product = productStock.product;
        return row("productCode", product.getProductCode(),
                "productName", product.getProductName(),
                "categoryName", product.getCategory() == null ? "" : product.getCategory().getName(),
                "batchNumber", product.getBatchNumber(),
                "expiryDate", product.getExpiryDate(),
                "reorderLevel", product.getReorderLevel(),
                "stockQuantity", productStock.stockQuantity,
                "inventoryValue", productStock.inventoryValue(),
                "stockStatus", productStock.stockQuantity.compareTo(safe(product.getReorderLevel())) <= 0 ? "LOW_STOCK" : "OK");
    }

    private ReportColumnDto col(String key, String label, String type) {
        return new ReportColumnDto(key, label, type);
    }

    private ReportMetricDto metric(String label, long value, String format) {
        return metric(label, BigDecimal.valueOf(value), format);
    }

    private ReportMetricDto metric(String label, BigDecimal value, String format) {
        return new ReportMetricDto(label, safe(value), format);
    }

    private Map<String, Object> row(Object... values) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < values.length - 1; i += 2) {
            map.put(String.valueOf(values[i]), values[i + 1]);
        }
        return map;
    }

    private BigDecimal sumOrders(List<Order> orders) {
        return sum(orders, Order::getNetAmount);
    }

    private BigDecimal sumInvoices(List<Invoice> invoices) {
        return sum(invoices, Invoice::getNetAmount);
    }

    private BigDecimal sumPayments(List<Payment> payments) {
        return sum(payments, Payment::getAmount);
    }

    private BigDecimal sumDeliveryCost(List<Delivery> deliveries) {
        return sum(deliveries, Delivery::getTransportCost);
    }

    private <T> BigDecimal sum(List<T> items, java.util.function.Function<T, BigDecimal> mapper) {
        return items.stream().map(mapper).map(this::safe).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal signedQuantity(StockTransaction tx) {
        BigDecimal quantity = safe(tx.getQuantity());
        if (EnumSet.of(StockTransactionType.OPENING, StockTransactionType.RECEIVE,
                StockTransactionType.RETURN_IN, StockTransactionType.ADJUSTMENT).contains(tx.getTransactionType())) {
            return quantity;
        }
        return quantity.negate();
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String nullSafe(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private String periodLabel(LocalDate fromDate, LocalDate toDate) {
        return fromDate.equals(toDate) ? String.valueOf(fromDate) : fromDate + " to " + toDate;
    }

    private static class RetailerSales {
        private final Retailer retailer;
        private long invoiceCount;
        private BigDecimal salesAmount = BigDecimal.ZERO;
        private BigDecimal paidAmount = BigDecimal.ZERO;
        private BigDecimal dueAmount = BigDecimal.ZERO;

        private RetailerSales(Retailer retailer) {
            this.retailer = retailer;
        }

        private void add(BigDecimal sales, BigDecimal paid, BigDecimal due) {
            invoiceCount++;
            salesAmount = salesAmount.add(sales == null ? BigDecimal.ZERO : sales);
            paidAmount = paidAmount.add(paid == null ? BigDecimal.ZERO : paid);
            dueAmount = dueAmount.add(due == null ? BigDecimal.ZERO : due);
        }
    }

    private static class ProductSales {
        private final Product product;
        private BigDecimal quantity = BigDecimal.ZERO;
        private BigDecimal salesAmount = BigDecimal.ZERO;

        private ProductSales(Product product) {
            this.product = product;
        }

        private void add(BigDecimal qty, BigDecimal amount) {
            quantity = quantity.add(qty == null ? BigDecimal.ZERO : qty);
            salesAmount = salesAmount.add(amount == null ? BigDecimal.ZERO : amount);
        }

        private BigDecimal purchaseCost() {
            BigDecimal purchase = product.getPurchasePrice() == null ? BigDecimal.ZERO : product.getPurchasePrice();
            return purchase.multiply(quantity);
        }

        private BigDecimal estimatedProfit() {
            return salesAmount.subtract(purchaseCost());
        }

        private BigDecimal averageRate() {
            if (quantity.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }
            return salesAmount.divide(quantity, 2, RoundingMode.HALF_UP);
        }
    }

    private static class BatchStock {
        private final Product product;
        private final Warehouse warehouse;
        private final String batchNumber;
        private final LocalDate expiryDate;
        private BigDecimal stockQuantity = BigDecimal.ZERO;

        private BatchStock(StockTransaction tx) {
            this.product = tx.getProduct();
            this.warehouse = tx.getWarehouse();
            this.batchNumber = tx.getBatchNumber();
            this.expiryDate = tx.getExpiryDate();
        }

        private void add(BigDecimal quantity) {
            stockQuantity = stockQuantity.add(quantity == null ? BigDecimal.ZERO : quantity);
        }
    }

    private static class ProductStock {
        private final Product product;
        private final BigDecimal stockQuantity;

        private ProductStock(Product product, BigDecimal stockQuantity) {
            this.product = product;
            this.stockQuantity = stockQuantity == null ? BigDecimal.ZERO : stockQuantity;
        }

        private BigDecimal inventoryValue() {
            BigDecimal purchasePrice = product.getPurchasePrice() == null ? BigDecimal.ZERO : product.getPurchasePrice();
            return purchasePrice.multiply(stockQuantity);
        }
    }
}
