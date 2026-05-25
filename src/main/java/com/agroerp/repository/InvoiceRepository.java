package com.agroerp.repository;

import com.agroerp.entity.Invoice;
import com.agroerp.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    long countByInvoiceDate(LocalDate invoiceDate);
    long countByStatus(InvoiceStatus status);
    boolean existsByInvoiceNumber(String invoiceNumber);

    @Query("select max(i.invoiceNumber) from Invoice i where i.invoiceNumber like concat(:prefix, '-%')")
    Optional<String> findMaxInvoiceNumberForPrefix(@Param("prefix") String prefix);

    @Query("""
            select distinct i from Invoice i
            join fetch i.retailer
            join fetch i.order
            left join fetch i.items item
            left join fetch item.product
            where i.id = :id
            """)
    Optional<Invoice> findPrintById(@Param("id") Long id);

    @Query("""
            select distinct i from Invoice i
            join fetch i.retailer
            left join fetch i.order
            where i.deleted = false
              and i.invoiceDate between :fromDate and :toDate
            order by i.invoiceDate desc, i.id desc
            """)
    List<Invoice> reportInvoices(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query("""
            select distinct i from Invoice i
            join fetch i.retailer
            left join fetch i.items item
            left join fetch item.product
            where i.deleted = false
              and i.invoiceDate between :fromDate and :toDate
            order by i.invoiceDate desc, i.id desc
            """)
    List<Invoice> reportInvoicesWithItems(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}
