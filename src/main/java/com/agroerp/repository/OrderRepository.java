package com.agroerp.repository;

import com.agroerp.entity.Order;
import com.agroerp.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    long countByOrderDate(LocalDate orderDate);
    long countByStatus(OrderStatus status);
    Page<Order> findAllByOrderByIdDesc(Pageable pageable);
    boolean existsByOrderNumber(String orderNumber);

    @Query("select max(o.orderNumber) from Order o where o.orderNumber like concat(:prefix, '-%')")
    Optional<String> findMaxOrderNumberForPrefix(@Param("prefix") String prefix);

    @Query("""
            select distinct o from Order o
            join fetch o.retailer
            where o.deleted = false
              and o.orderDate between :fromDate and :toDate
            order by o.orderDate desc, o.id desc
            """)
    List<Order> reportOrders(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}
