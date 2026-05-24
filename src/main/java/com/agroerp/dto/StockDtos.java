package com.agroerp.dto;

import com.agroerp.enums.StockTransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class StockDtos {
    private StockDtos() {}

    public record StockTransactionRequest(@NotNull Long productId, @NotNull Long warehouseId,
                                          StockTransactionType transactionType,
                                          String movementTypeCode,
                                          @NotNull @Positive BigDecimal quantity,
                                          String batchNumber, LocalDate expiryDate, String remarks) {}
    public record StockBalanceResponse(Long productId, Long warehouseId, BigDecimal currentStock) {}
    public record StockMovementResponse(Long id, Long productId, String productCode, String productName,
                                        Long warehouseId, String warehouseName, StockTransactionType transactionType,
                                        String movementTypeCode, String movementTypeName, BigDecimal quantity,
                                        String batchNumber, LocalDate expiryDate, String referenceType,
                                        Long referenceId, String remarks, java.time.Instant createdAt) {}
}
