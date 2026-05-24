package com.agroerp.serviceImpl;

import com.agroerp.entity.AccountLedger;
import com.agroerp.entity.ChartOfAccount;
import com.agroerp.exception.ResourceNotFoundException;
import com.agroerp.repository.AccountLedgerRepository;
import com.agroerp.repository.ChartOfAccountRepository;
import com.agroerp.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final ChartOfAccountRepository accountRepository;
    private final AccountLedgerRepository ledgerRepository;

    public AccountServiceImpl(ChartOfAccountRepository accountRepository, AccountLedgerRepository ledgerRepository) {
        this.accountRepository = accountRepository;
        this.ledgerRepository = ledgerRepository;
    }

    @Override
    public ChartOfAccount account(String accountCode) {
        return accountRepository.findByAccountCode(accountCode)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountCode));
    }

    @Override
    @Transactional
    public void post(String accountCode, LocalDate date, BigDecimal debit, BigDecimal credit,
                     String referenceType, Long referenceId, String narration) {
        ChartOfAccount account = account(accountCode);
        BigDecimal previous = ledgerRepository.findByDeletedFalseOrderByTransactionDateAscIdAsc().stream()
                .filter(l -> l.getAccount().getId().equals(account.getId()))
                .reduce((first, second) -> second)
                .map(AccountLedger::getRunningBalance)
                .orElse(BigDecimal.ZERO);
        AccountLedger ledger = new AccountLedger();
        ledger.setAccount(account);
        ledger.setTransactionDate(date);
        ledger.setDebitAmount(debit);
        ledger.setCreditAmount(credit);
        ledger.setRunningBalance(previous.add(debit).subtract(credit));
        ledger.setReferenceType(referenceType);
        ledger.setReferenceId(referenceId);
        ledger.setNarration(narration);
        ledgerRepository.save(ledger);
    }

    @Override
    public List<ChartOfAccount> accounts() {
        return accountRepository.findAll();
    }

    @Override
    public List<AccountLedger> ledger() {
        return ledgerRepository.findByDeletedFalseOrderByTransactionDateAscIdAsc();
    }
}
