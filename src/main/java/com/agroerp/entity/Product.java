package com.agroerp.entity;

import com.agroerp.enums.MaterialType;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product extends BaseEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String productCode;
    @Column(nullable = false, length = 180)
    private String productName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaterialType materialType = MaterialType.FINISHED_PRODUCTS;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "brand_id")
    private Brand brand;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "unit_id")
    private Unit unit;
    private String packSize;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal purchasePrice = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal salesPrice = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal retailerPrice = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2, columnDefinition = "decimal(14,2) default 0")
    private BigDecimal currentPrice = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 3, columnDefinition = "decimal(14,3) default 0")
    private BigDecimal totalStockQuantity = BigDecimal.ZERO;
    @Column(nullable = false, precision = 16, scale = 2, columnDefinition = "decimal(16,2) default 0")
    private BigDecimal currentInventoryValue = BigDecimal.ZERO;
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal vatPercent = BigDecimal.ZERO;
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal discountPercent = BigDecimal.ZERO;
    private String batchNumber;
    private LocalDate expiryDate;
    private String imageUrl;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal reorderLevel = BigDecimal.ZERO;
}

