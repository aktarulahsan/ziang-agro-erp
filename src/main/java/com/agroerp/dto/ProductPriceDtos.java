package com.agroerp.dto;

import com.agroerp.enums.AccountingEntryType;
import com.agroerp.enums.PriceConditionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public final class ProductPriceDtos {
    private ProductPriceDtos() {}

    public record PriceConditionRequest(@NotNull PriceConditionType conditionType,
                                        @NotNull @PositiveOrZero BigDecimal amount,
                                        String description) {}

    public record ProductPriceUpdateRequest(@NotNull @PositiveOrZero BigDecimal newPrice,
                                            @NotBlank String reason,
                                            @NotBlank String changedBy,
                                            @Valid List<PriceConditionRequest> conditions) {}

    public record ProductPriceUpdateResponse(String message,
                                             BigDecimal oldPrice,
                                             BigDecimal newPrice,
                                             BigDecimal requestedPrice,
                                             BigDecimal totalStockQuantity,
                                             BigDecimal inventoryValueBefore,
                                             BigDecimal inventoryValueAfter,
                                             BigDecimal inventoryValueChange,
                                             String materialDocumentNumber) {}

    public record BulkProductPriceUpdateItem(@NotBlank String productCode,
                                             @NotNull @PositiveOrZero BigDecimal newPrice,
                                             @NotBlank String reason,
                                             @NotBlank String changedBy,
                                             @Valid List<PriceConditionRequest> conditions) {}

    public record BulkProductPriceUpdateRequest(@Valid List<BulkProductPriceUpdateItem> items) {}

    public record PriceChangeHistoryResponse(Long id,
                                             String productCode,
                                             String productName,
                                             BigDecimal oldPrice,
                                             BigDecimal newPrice,
                                             BigDecimal requestedPrice,
                                             String changeReason,
                                             String changedBy,
                                             Instant changeDateTime,
                                             BigDecimal inventoryValueBefore,
                                             BigDecimal inventoryValueAfter,
                                             BigDecimal inventoryValueChange,
                                             String conditionSummary,
                                             String materialDocumentNumber) {}

    public record AccountingEntryResponse(Long id,
                                          String productCode,
                                          String productName,
                                          AccountingEntryType entryType,
                                          BigDecimal amount,
                                          String description,
                                          LocalDate postingDate,
                                          String materialDocumentNumber) {}
}
