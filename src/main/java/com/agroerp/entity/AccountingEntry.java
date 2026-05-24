package com.agroerp.entity;

import com.agroerp.enums.AccountingEntryType;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "accounting_entries", indexes = {
        @Index(name = "idx_accounting_entry_product_date", columnList = "product_id, posting_date"),
        @Index(name = "idx_accounting_entry_document", columnList = "material_document_number")
})
@Getter
@Setter
public class AccountingEntry extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountingEntryType entryType;

    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private LocalDate postingDate = LocalDate.now();

    @Column(name = "material_document_number", nullable = false, length = 50)
    private String materialDocumentNumber;
}
