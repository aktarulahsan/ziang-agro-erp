package com.agroerp.controller;

import com.agroerp.dto.AccountDtos.AccountLedgerResponse;
import com.agroerp.dto.AccountDtos.AccountResponse;
import com.agroerp.entity.AccountLedger;
import com.agroerp.entity.ChartOfAccount;
import com.agroerp.response.ApiResponse;
import com.agroerp.service.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ACCOUNTS_USER')")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ApiResponse<List<AccountResponse>> accounts() {
        return ApiResponse.ok("Accounts loaded", accountService.accounts().stream().map(this::toAccount).toList());
    }

    @GetMapping("/ledger")
    @Transactional(readOnly = true)
    public ApiResponse<List<AccountLedgerResponse>> ledger() {
        return ApiResponse.ok("Account ledger loaded", accountService.ledger().stream().map(this::toLedger).toList());
    }

    private AccountResponse toAccount(ChartOfAccount account) {
        return new AccountResponse(account.getId(), account.getAccountCode(), account.getAccountName(),
                account.getAccountType(), account.isSystemAccount(), account.isActive());
    }

    private AccountLedgerResponse toLedger(AccountLedger ledger) {
        return new AccountLedgerResponse(ledger.getId(), ledger.getTransactionDate(),
                ledger.getAccount().getAccountCode(), ledger.getAccount().getAccountName(),
                ledger.getDebitAmount(), ledger.getCreditAmount(), ledger.getRunningBalance(),
                ledger.getReferenceType(), ledger.getReferenceId(), ledger.getNarration());
    }
}
