package com.agroerp.repository;

import com.agroerp.entity.Invoice;
import com.agroerp.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    long countByInvoiceDate(LocalDate invoiceDate);
    long countByStatus(InvoiceStatus status);
    boolean existsByInvoiceNumber(String invoiceNumber);

    @Query("select max(i.invoiceNumber) from Invoice i where i.invoiceNumber like concat(:prefix, '-%')")
    Optional<String> findMaxInvoiceNumberForPrefix(@Param("prefix") String prefix);
}
