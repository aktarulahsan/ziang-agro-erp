package com.agroerp.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "material_price_history", indexes = {
        @Index(name = "idx_price_history_product_date", columnList = "product_id, posting_date")
})
@Getter
@Setter
public class MaterialPriceHistory extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private LocalDate postingDate = LocalDate.now();

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal previousPurchasePrice = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal currentPurchasePrice = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal previousSalesPrice = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal currentSalesPrice = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal previousRetailerPrice = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal currentRetailerPrice = BigDecimal.ZERO;
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal previousVatPercent = BigDecimal.ZERO;
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal currentVatPercent = BigDecimal.ZERO;
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal previousDiscountPercent = BigDecimal.ZERO;
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal currentDiscountPercent = BigDecimal.ZERO;

    @Column(length = 255)
    private String remarks;
}
