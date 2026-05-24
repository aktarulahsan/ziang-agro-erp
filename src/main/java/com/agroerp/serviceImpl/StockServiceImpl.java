package com.agroerp.serviceImpl;

import com.agroerp.dto.StockDtos.StockBalanceResponse;
import com.agroerp.dto.StockDtos.StockTransactionRequest;
import com.agroerp.entity.Product;
import com.agroerp.entity.StockTransaction;
import com.agroerp.entity.Warehouse;
import com.agroerp.enums.StockTransactionType;
import com.agroerp.exception.BusinessException;
import com.agroerp.exception.ResourceNotFoundException;
import com.agroerp.repository.ProductRepository;
import com.agroerp.repository.StockTransactionRepository;
import com.agroerp.repository.WarehouseRepository;
import com.agroerp.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class StockServiceImpl implements StockService {
    private final StockTransactionRepository stockRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    public StockServiceImpl(StockTransactionRepository stockRepository, ProductRepository productRepository,
                            WarehouseRepository warehouseRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    @Transactional
    public void postTransaction(StockTransactionRequest request, String referenceType, Long referenceId) {
        Product product = productRepository.findById(request.productId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Warehouse warehouse = warehouseRepository.findById(request.warehouseId()).orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
        Movement movement = movementFrom(request);
        if (isOutbound(movement.transactionType())) {
            ensureAvailable(request.productId(), request.warehouseId(), request.quantity());
        }
        StockTransaction tx = new StockTransaction();
        tx.setProduct(product);
        tx.setWarehouse(warehouse);
        tx.setTransactionType(movement.transactionType());
        tx.setMovementTypeCode(movement.code());
        tx.setMovementTypeName(movement.name());
        tx.setQuantity(request.quantity());
        tx.setBatchNumber(request.batchNumber());
        tx.setExpiryDate(request.expiryDate());
        tx.setRemarks(request.remarks());
        tx.setReferenceType(referenceType);
        tx.setReferenceId(referenceId);
        stockRepository.save(tx);
    }

    @Override
    public BigDecimal currentStock(Long productId, Long warehouseId) {
        return stockRepository.currentStock(productId, warehouseId);
    }

    @Override
    public StockBalanceResponse balance(Long productId, Long warehouseId) {
        return new StockBalanceResponse(productId, warehouseId, currentStock(productId, warehouseId));
    }

    @Override
    public void ensureAvailable(Long productId, Long warehouseId, BigDecimal quantity) {
        BigDecimal current = currentStock(productId, warehouseId);
        if (current.compareTo(quantity) < 0) {
            throw new BusinessException("Insufficient stock. Available: " + current + ", required: " + quantity);
        }
    }

    @Override
    @Transactional
    public void stockOut(Long productId, Long warehouseId, BigDecimal quantity, StockTransactionType type, String referenceType, Long referenceId) {
        postTransaction(new StockTransactionRequest(productId, warehouseId, type, null, quantity, null, null, "Auto stock out"),
                referenceType, referenceId);
    }

    private boolean isOutbound(StockTransactionType type) {
        return type == StockTransactionType.ISSUE || type == StockTransactionType.SALES_OUT || type == StockTransactionType.DAMAGE;
    }

    private Movement movementFrom(StockTransactionRequest request) {
        String code = request.movementTypeCode();
        if (code != null && !code.isBlank()) {
            return switch (code.trim()) {
                case "101" -> new Movement("101", "Goods Receipt", StockTransactionType.RECEIVE);
                case "102" -> new Movement("102", "Reversal of Goods Receipt", StockTransactionType.ISSUE);
                case "201" -> new Movement("201", "Goods Issue", StockTransactionType.ISSUE);
                case "261" -> new Movement("261", "Issue to Production", StockTransactionType.ISSUE);
                case "309" -> new Movement("309", "Transfer / Adjustment", StockTransactionType.ADJUSTMENT);
                case "501" -> new Movement("501", "Initial Entry of Stock", StockTransactionType.OPENING);
                case "551" -> new Movement("551", "Scrapping / Damage", StockTransactionType.DAMAGE);
                case "653" -> new Movement("653", "Customer Return to Stock", StockTransactionType.RETURN_IN);
                default -> throw new BusinessException("Unsupported movement type: " + code);
            };
        }
        StockTransactionType type = request.transactionType();
        if (type == null) {
            throw new BusinessException("Movement type is required");
        }
        return switch (type) {
            case OPENING -> new Movement("501", "Initial Entry of Stock", type);
            case RECEIVE -> new Movement("101", "Goods Receipt", type);
            case ISSUE -> new Movement("201", "Goods Issue", type);
            case SALES_OUT -> new Movement("601", "Goods Issue for Delivery", type);
            case RETURN_IN -> new Movement("653", "Customer Return to Stock", type);
            case DAMAGE -> new Movement("551", "Scrapping / Damage", type);
            case ADJUSTMENT -> new Movement("309", "Transfer / Adjustment", type);
        };
    }

    private record Movement(String code, String name, StockTransactionType transactionType) {}
}
