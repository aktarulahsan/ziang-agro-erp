package com.agroerp.service;

import com.agroerp.dto.StockDtos.StockBalanceResponse;
import com.agroerp.dto.StockDtos.StockTransactionRequest;
import com.agroerp.enums.StockTransactionType;

import java.math.BigDecimal;

public interface StockService {
    void postTransaction(StockTransactionRequest request, String referenceType, Long referenceId);
    BigDecimal currentStock(Long productId, Long warehouseId);
    StockBalanceResponse balance(Long productId, Long warehouseId);
    void ensureAvailable(Long productId, Long warehouseId, BigDecimal quantity);
    void stockOut(Long productId, Long warehouseId, BigDecimal quantity, StockTransactionType type, String referenceType, Long referenceId);
}
