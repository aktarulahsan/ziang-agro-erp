package com.agroerp.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "delivery_items")
@Getter
@Setter
public class DeliveryItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal deliveredQuantity = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal pendingQuantity = BigDecimal.ZERO;
}

