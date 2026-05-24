package com.agroerp.repository;

import com.agroerp.entity.ChartOfAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccount, Long> {
    Optional<ChartOfAccount> findByAccountCode(String accountCode);
}
