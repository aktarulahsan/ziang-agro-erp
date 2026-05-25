package com.agroerp.repository;

import com.agroerp.entity.RetailerLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RetailerLedgerRepository extends JpaRepository<RetailerLedger, Long> {
    List<RetailerLedger> findByRetailerIdAndDeletedFalseOrderByTransactionDateAscIdAsc(Long retailerId);

    @Query("""
            select ledger from RetailerLedger ledger
            join fetch ledger.retailer
            where ledger.deleted = false
              and (:retailerId is null or ledger.retailer.id = :retailerId)
              and ledger.transactionDate between :fromDate and :toDate
            order by ledger.transactionDate asc, ledger.id asc
            """)
    List<RetailerLedger> reportLedger(@Param("retailerId") Long retailerId,
                                      @Param("fromDate") LocalDate fromDate,
                                      @Param("toDate") LocalDate toDate);
}
