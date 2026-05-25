package com.agroerp.repository;

import com.agroerp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select coalesce(sum(p.amount), 0) from Payment p where p.paymentDate = :date and p.deleted = false")
    BigDecimal totalCollection(@Param("date") LocalDate date);
    boolean existsByReceiptNumber(String receiptNumber);

    @Query("select max(p.receiptNumber) from Payment p where p.receiptNumber like concat(:prefix, '-%')")
    Optional<String> findMaxReceiptNumberForPrefix(@Param("prefix") String prefix);

    @Query("""
            select p from Payment p
            join fetch p.retailer
            left join fetch p.invoice
            where p.deleted = false
              and p.paymentDate between :fromDate and :toDate
            order by p.paymentDate desc, p.id desc
            """)
    List<Payment> reportPayments(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}
