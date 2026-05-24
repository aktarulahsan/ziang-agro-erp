package com.agroerp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "account_ledger", indexes = @Index(name = "idx_account_ledger_date", columnList = "transaction_date, account_id"))
@Getter
@Setter
public class AccountLedger extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private ChartOfAccount account;

    @Column(nullable = false)
    private LocalDate transactionDate;

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
