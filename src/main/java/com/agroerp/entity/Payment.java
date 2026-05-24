package com.agroerp.entity;

import lombok.Getter;
import lombok.Setter;

import com.agroerp.enums.PaymentMethod;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends BaseEntity {
    @Column(nullable = false, unique = true, length = 40)
    private String receiptNumber;
    @Column(nullable = false)
    private LocalDate paymentDate;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "retailer_id", nullable = false)
    private Retailer retailer;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "invoice_id")
    private Invoice invoice;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;
    private String referenceNumber;
    private boolean approved;
    private String remarks;
}

