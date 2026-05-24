package com.agroerp.repository;

import com.agroerp.entity.MaterialPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialPriceHistoryRepository extends JpaRepository<MaterialPriceHistory, Long> {
    List<MaterialPriceHistory> findTop100ByOrderByPostingDateDescIdDesc();
    List<MaterialPriceHistory> findTop100ByProductIdOrderByPostingDateDescIdDesc(Long productId);
}
