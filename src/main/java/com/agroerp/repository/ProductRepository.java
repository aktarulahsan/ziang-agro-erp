package com.agroerp.repository;

import com.agroerp.entity.Product;
import com.agroerp.enums.MaterialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductCode(String productCode);
    Optional<Product> findByProductCode(String productCode);
    Page<Product> findByDeletedFalseAndProductNameContainingIgnoreCaseOrDeletedFalseAndProductCodeContainingIgnoreCase(String name, String code, Pageable pageable);
    List<Product> findByDeletedFalseOrderByProductNameAsc();
    List<Product> findByDeletedFalseAndMaterialTypeOrderByProductNameAsc(MaterialType materialType);
}
