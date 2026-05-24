package com.agroerp.mapper;

import com.agroerp.dto.ProductDto;
import com.agroerp.entity.Product;

public final class ProductMapper {
    private ProductMapper() {}

    public static ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(), product.getProductCode(), product.getProductName(),
                product.getMaterialType(),
                product.getCategory() == null ? null : product.getCategory().getId(),
                product.getSubCategory() == null ? null : product.getSubCategory().getId(),
                product.getBrand() == null ? null : product.getBrand().getId(),
                product.getUnit() == null ? null : product.getUnit().getId(),
                product.getPackSize(), product.getPurchasePrice(), product.getSalesPrice(), product.getRetailerPrice(),
                product.getVatPercent(), product.getDiscountPercent(), product.getBatchNumber(), product.getExpiryDate(),
                product.getImageUrl(), product.getReorderLevel(), product.isActive()
        );
    }
}
