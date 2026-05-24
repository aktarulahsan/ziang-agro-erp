package com.agroerp.dto;

import com.agroerp.enums.MaterialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductDto(
        Long id,
        @NotBlank String productCode,
        @NotBlank String productName,
        MaterialType materialType,
        Long categoryId,
        Long subCategoryId,
        Long brandId,
        Long unitId,
        String packSize,
        @NotNull BigDecimal purchasePrice,
        @NotNull BigDecimal salesPrice,
        @NotNull BigDecimal retailerPrice,
        BigDecimal vatPercent,
        BigDecimal discountPercent,
        String batchNumber,
        LocalDate expiryDate,
        String imageUrl,
        BigDecimal reorderLevel,
        boolean active
) {}
