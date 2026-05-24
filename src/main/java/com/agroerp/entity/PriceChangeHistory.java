package com.agroerp.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "price_change_history", indexes = {
        @Index(name = "idx_price_change_product_time", columnList = "product_id, change_date_time")
})
@Getter
@Setter
public class PriceChangeHistory extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal oldPrice = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal newPrice = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal requestedPrice = BigDecimal.ZERO;
    @Column(length = 500)
    private String changeReason;
    @Column(length = 100)
    private String changedBy;
    @Column(nullable = false)
    private Instant changeDateTime = Instant.now();
    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal inventoryValueBefore = BigDecimal.ZERO;
    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal inventoryValueAfter = BigDecimal.ZERO;
    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal inventoryValueChange = BigDecimal.ZERO;
    @Column(length = 1000)
    private String conditionSummary;
    @Column(length = 50)
    private String materialDocumentNumber;
}
