package com.agroerp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RetailerDto(
        Long id,
        @NotBlank String retailerCode,
        @NotBlank String retailerName,
        String ownerName,
        @NotBlank String mobileNumber,
        String email,
        String address,
        Long territoryId,
        String marketName,
        @NotNull BigDecimal creditLimit,
        @NotNull BigDecimal openingBalance,
        BigDecimal currentDueBalance,
        boolean active
) {}
