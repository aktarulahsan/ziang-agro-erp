package com.agroerp.repository;

import com.agroerp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select coalesce(sum(p.amount), 0) from Payment p where p.paymentDate = :date and p.deleted = false")
    BigDecimal totalCollection(@Param("date") LocalDate date);
    boolean existsByReceiptNumber(String receiptNumber);

    @Query("select max(p.receiptNumber) from Payment p where p.receiptNumber like concat(:prefix, '-%')")
    Optional<String> findMaxReceiptNumberForPrefix(@Param("prefix") String prefix);
}
