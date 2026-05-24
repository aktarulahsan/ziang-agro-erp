package com.agroerp.dto;

import java.math.BigDecimal;

public record DashboardSummaryDto(
        long todaysOrders,
        long todaysInvoices,
        long todaysDeliveries,
        BigDecimal todaysCollection,
        long totalRetailers,
        long totalProducts,
        long pendingInvoices,
        long pendingDeliveries,
        long lowStockItems,
        BigDecimal retailerDueAmount
) {}
