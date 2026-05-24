package com.agroerp.dto;

import com.agroerp.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class AccountDtos {
    private AccountDtos() {}

    public record AccountResponse(Long id, String accountCode, String accountName, AccountType accountType,
                                  boolean systemAccount, boolean active) {}

    public record AccountLedgerResponse(Long id, LocalDate transactionDate, String accountCode, String accountName,
                                        BigDecimal debitAmount, BigDecimal creditAmount, BigDecimal runningBalance,
                                        String referenceType, Long referenceId, String narration) {}
}
