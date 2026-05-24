package com.agroerp.repository;

import com.agroerp.entity.Retailer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetailerRepository extends JpaRepository<Retailer, Long> {
    boolean existsByRetailerCode(String retailerCode);
    Page<Retailer> findByDeletedFalseAndRetailerNameContainingIgnoreCaseOrDeletedFalseAndMobileNumberContainingIgnoreCase(String name, String mobile, Pageable pageable);
}
