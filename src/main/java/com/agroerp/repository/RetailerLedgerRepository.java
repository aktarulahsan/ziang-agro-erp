package com.agroerp.repository;

import com.agroerp.entity.RetailerLedger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RetailerLedgerRepository extends JpaRepository<RetailerLedger, Long> {
    List<RetailerLedger> findByRetailerIdAndDeletedFalseOrderByTransactionDateAscIdAsc(Long retailerId);
}
