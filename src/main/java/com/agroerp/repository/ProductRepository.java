package com.agroerp.repository;

import com.agroerp.entity.Product;
import com.agroerp.enums.MaterialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductCode(String productCode);
    Optional<Product> findByProductCode(String productCode);
    Page<Product> findByDeletedFalseAndProductNameContainingIgnoreCaseOrDeletedFalseAndProductCodeContainingIgnoreCase(String name, String code, Pageable pageable);
    List<Product> findByDeletedFalseOrderByProductNameAsc();
    List<Product> findByDeletedFalseAndMaterialTypeOrderByProductNameAsc(MaterialType materialType);
    List<Product> findByDeletedFalseAndExpiryDateBetweenOrderByExpiryDateAsc(LocalDate fromDate, LocalDate toDate);

    @Query("select max(p.productCode) from Product p where p.productCode like concat(:prefix, '-%')")
    Optional<String> findMaxProductCodeForPrefix(@Param("prefix") String prefix);
}
