package com.agroerp.mapper;

import com.agroerp.dto.RetailerDto;
import com.agroerp.entity.Retailer;

public final class RetailerMapper {
    private RetailerMapper() {}

    public static RetailerDto toDto(Retailer retailer) {
        return new RetailerDto(
                retailer.getId(), retailer.getRetailerCode(), retailer.getRetailerName(), retailer.getOwnerName(),
                retailer.getMobileNumber(), retailer.getEmail(), retailer.getAddress(),
                retailer.getTerritory() == null ? null : retailer.getTerritory().getId(),
                retailer.getMarketName(), retailer.getCreditLimit(), retailer.getOpeningBalance(),
                retailer.getCurrentDueBalance(), retailer.isActive()
        );
    }
}
