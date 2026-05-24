package com.agroerp.entity;

import com.agroerp.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chart_of_accounts")
@Getter
@Setter
public class ChartOfAccount extends BaseEntity {
    @Column(nullable = false, unique = true, length = 40)
    private String accountCode;

    @Column(nullable = false, length = 160)
    private String accountName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ChartOfAccount parent;

    private boolean systemAccount;
}
