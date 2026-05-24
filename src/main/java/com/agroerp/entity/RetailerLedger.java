package com.agroerp.entity;

import lombok.Getter;
import lombok.Setter;

import com.agroerp.enums.LedgerType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "retailer_ledger", indexes = @Index(name = "idx_ledger_retailer_date", columnList = "retailer_id, transaction_date"))
@Getter
@Setter
public class RetailerLedger extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "retailer_id", nullable = false)
    private Retailer retailer;
    @Column(nullable = false)
    private LocalDate transactionDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerType ledgerType;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal debitAmount = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal creditAmount = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal runningBalance = BigDecimal.ZERO;
    private String referenceType;
    private Long referenceId;
    private String narration;
}

