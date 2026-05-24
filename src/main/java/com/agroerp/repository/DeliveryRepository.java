package com.agroerp.repository;

import com.agroerp.entity.Delivery;
import com.agroerp.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    long countByDeliveryDate(LocalDate deliveryDate);
    long countByStatus(DeliveryStatus status);
    boolean existsByInvoiceIdAndStatusNot(Long invoiceId, DeliveryStatus status);
    List<Delivery> findAllByOrderByIdDesc();
    List<Delivery> findByStatusNot(DeliveryStatus status);
    boolean existsByDeliveryNumber(String deliveryNumber);

    @Query("select max(d.deliveryNumber) from Delivery d where d.deliveryNumber like concat(:prefix, '-%')")
    Optional<String> findMaxDeliveryNumberForPrefix(@Param("prefix") String prefix);
}
