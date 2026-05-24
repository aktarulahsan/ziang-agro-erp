package com.agroerp.entity;

import lombok.Getter;
import lombok.Setter;

import com.agroerp.enums.StockTransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock_transactions", indexes = {
        @Index(name = "idx_stock_product_warehouse", columnList = "product_id, warehouse_id"),
        @Index(name = "idx_stock_reference", columnList = "reference_type, reference_id")
})
@Getter
@Setter
public class StockTransaction extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockTransactionType transactionType;
    @Column(length = 10)
    private String movementTypeCode;
    @Column(length = 120)
    private String movementTypeName;
    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal quantity = BigDecimal.ZERO;
    private String batchNumber;
    private LocalDate expiryDate;
    private String referenceType;
    private Long referenceId;
    private String remarks;
}

