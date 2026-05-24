package com.agroerp.repository;

import com.agroerp.entity.AccountLedger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountLedgerRepository extends JpaRepository<AccountLedger, Long> {
    List<AccountLedger> findByDeletedFalseOrderByTransactionDateAscIdAsc();
}
