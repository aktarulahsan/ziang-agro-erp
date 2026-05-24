package com.agroerp.entity;

import lombok.Getter;
import lombok.Setter;

import com.agroerp.enums.InvoiceStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
public class Invoice extends BaseEntity {
    @Column(nullable = false, unique = true, length = 40)
    private String invoiceNumber;
    @Column(nullable = false)
    private LocalDate invoiceDate;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "retailer_id", nullable = false)
    private Retailer retailer;
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status = InvoiceStatus.PENDING;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal netAmount = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal dueAmount = BigDecimal.ZERO;
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> items = new ArrayList<>();
}

