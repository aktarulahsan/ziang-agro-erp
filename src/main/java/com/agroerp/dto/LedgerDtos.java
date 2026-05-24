package com.agroerp.dto;

import com.agroerp.enums.LedgerType;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class LedgerDtos {
    private LedgerDtos() {}

    public record LedgerEntryResponse(Long id, LocalDate transactionDate, LedgerType ledgerType,
                                      BigDecimal debitAmount, BigDecimal creditAmount,
                                      BigDecimal runningBalance, String referenceType,
                                      Long referenceId, String narration) {}
}
