package com.agroerp.repository;

import com.agroerp.entity.Retailer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RetailerRepository extends JpaRepository<Retailer, Long> {
    boolean existsByRetailerCode(String retailerCode);
    Page<Retailer> findByDeletedFalseAndRetailerNameContainingIgnoreCaseOrDeletedFalseAndMobileNumberContainingIgnoreCase(String name, String mobile, Pageable pageable);
    List<Retailer> findByDeletedFalseOrderByRetailerNameAsc();

    @Query("select max(r.retailerCode) from Retailer r where r.retailerCode like concat(:prefix, '-%')")
    Optional<String> findMaxRetailerCodeForPrefix(@Param("prefix") String prefix);
}
