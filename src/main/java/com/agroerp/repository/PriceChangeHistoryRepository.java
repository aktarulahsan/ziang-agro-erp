package com.agroerp.repository;

import com.agroerp.entity.PriceChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceChangeHistoryRepository extends JpaRepository<PriceChangeHistory, Long> {
    List<PriceChangeHistory> findByProductProductCodeOrderByChangeDateTimeDescIdDesc(String productCode);
}
