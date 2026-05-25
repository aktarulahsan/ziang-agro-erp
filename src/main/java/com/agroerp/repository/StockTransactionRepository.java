package com.agroerp.repository;

import com.agroerp.entity.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    @Query(value = """
            select coalesce(sum(case
                when transaction_type in ('OPENING','RECEIVE','RETURN_IN','ADJUSTMENT') then quantity
                else -quantity end), 0)
            from stock_transactions
            where product_id = :productId and warehouse_id = :warehouseId and deleted = false
            """, nativeQuery = true)
    BigDecimal currentStock(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId);

    @Query(value = """
            select coalesce(sum(case
                when transaction_type in ('OPENING','RECEIVE','RETURN_IN','ADJUSTMENT') then quantity
                else -quantity end), 0)
            from stock_transactions
            where product_id = :productId and deleted = false
            """, nativeQuery = true)
    BigDecimal currentStockAllWarehouses(@Param("productId") Long productId);

    List<StockTransaction> findTop50ByDeletedFalseOrderByCreatedAtDesc();

    @Query("""
            select tx from StockTransaction tx
            join fetch tx.product
            join fetch tx.warehouse
            where tx.deleted = false
              and (:productId is null or tx.product.id = :productId)
              and (:movementTypeCode is null or tx.movementTypeCode = :movementTypeCode)
            order by tx.createdAt desc, tx.id desc
            """)
    List<StockTransaction> movementHistory(@Param("productId") Long productId,
                                           @Param("movementTypeCode") String movementTypeCode);

    @Query("""
            select tx from StockTransaction tx
            join fetch tx.product
            join fetch tx.warehouse
            where tx.deleted = false
            order by tx.product.productName asc, tx.batchNumber asc, tx.expiryDate asc
            """)
    List<StockTransaction> reportBatchMovements();
}
