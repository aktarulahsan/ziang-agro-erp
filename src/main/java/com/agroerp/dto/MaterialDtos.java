package com.agroerp.dto;

import com.agroerp.enums.MaterialType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public final class MaterialDtos {
    private MaterialDtos() {}

    public record MaterialStockSummary(
            Long id,
            String productCode,
            String productName,
            MaterialType materialType,
            Long unitId,
            String packSize,
            BigDecimal purchasePrice,
            BigDecimal salesPrice,
            BigDecimal retailerPrice,
            BigDecimal vatPercent,
            BigDecimal discountPercent,
            BigDecimal reorderLevel,
            Long warehouseId,
            BigDecimal currentStock
    ) {}

    public record MaterialPriceRequest(
            @NotNull BigDecimal purchasePrice,
            @NotNull BigDecimal salesPrice,
            @NotNull BigDecimal retailerPrice,
            BigDecimal vatPercent,
            BigDecimal discountPercent,
            String remarks
    ) {}

    public record MaterialPriceHistoryResponse(
            Long id,
            Long productId,
            String productCode,
            String productName,
            LocalDate postingDate,
            BigDecimal previousPurchasePrice,
            BigDecimal currentPurchasePrice,
            BigDecimal previousSalesPrice,
            BigDecimal currentSalesPrice,
            BigDecimal previousRetailerPrice,
            BigDecimal currentRetailerPrice,
            BigDecimal previousVatPercent,
            BigDecimal currentVatPercent,
            BigDecimal previousDiscountPercent,
            BigDecimal currentDiscountPercent,
            String remarks,
            Instant createdAt
    ) {}
}
