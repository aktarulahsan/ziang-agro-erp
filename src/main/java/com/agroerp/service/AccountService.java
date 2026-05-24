package com.agroerp.service;

import com.agroerp.entity.AccountLedger;
import com.agroerp.entity.ChartOfAccount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AccountService {
    ChartOfAccount account(String accountCode);
    void post(String accountCode, LocalDate date, BigDecimal debit, BigDecimal credit,
              String referenceType, Long referenceId, String narration);
    List<ChartOfAccount> accounts();
    List<AccountLedger> ledger();
}
