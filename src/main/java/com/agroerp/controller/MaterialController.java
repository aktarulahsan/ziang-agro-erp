package com.agroerp.controller;

import com.agroerp.dto.MaterialDtos.MaterialPriceRequest;
import com.agroerp.dto.MaterialDtos.MaterialPriceHistoryResponse;
import com.agroerp.dto.MaterialDtos.MaterialStockSummary;
import com.agroerp.dto.ProductDto;
import com.agroerp.dto.StockDtos.StockMovementResponse;
import com.agroerp.entity.MaterialPriceHistory;
import com.agroerp.entity.Product;
import com.agroerp.entity.StockTransaction;
import com.agroerp.enums.MaterialType;
import com.agroerp.exception.ResourceNotFoundException;
import com.agroerp.mapper.ProductMapper;
import com.agroerp.repository.MaterialPriceHistoryRepository;
import com.agroerp.repository.ProductRepository;
import com.agroerp.repository.StockTransactionRepository;
import com.agroerp.repository.WarehouseRepository;
import com.agroerp.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final StockTransactionRepository stockRepository;
    private final MaterialPriceHistoryRepository priceHistoryRepository;

    public MaterialController(ProductRepository productRepository, WarehouseRepository warehouseRepository,
                              StockTransactionRepository stockRepository, MaterialPriceHistoryRepository priceHistoryRepository) {
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.stockRepository = stockRepository;
        this.priceHistoryRepository = priceHistoryRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ApiResponse<List<ProductDto>> list(@RequestParam(required = false) MaterialType materialType) {
        List<Product> rows = materialType == null
                ? productRepository.findByDeletedFalseOrderByProductNameAsc()
                : productRepository.findByDeletedFalseAndMaterialTypeOrderByProductNameAsc(materialType);
        return ApiResponse.ok("Materials loaded", rows.stream().map(ProductMapper::toDto).toList());
    }

    @GetMapping("/stock-summary")
    @Transactional(readOnly = true)
    public ApiResponse<List<MaterialStockSummary>> stockSummary(@RequestParam Long warehouseId,
                                                                @RequestParam(required = false) MaterialType materialType) {
        warehouseRepository.findById(warehouseId).orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
        List<Product> rows = materialType == null
                ? productRepository.findByDeletedFalseOrderByProductNameAsc()
                : productRepository.findByDeletedFalseAndMaterialTypeOrderByProductNameAsc(materialType);
        return ApiResponse.ok("Material stock summary loaded", rows.stream()
                .map(product -> toStockSummary(product, warehouseId))
                .toList());
    }

    @PutMapping("/{id}/price")
    @Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','STORE_USER')")
    public ApiResponse<ProductDto> updatePrice(@PathVariable Long id, @Valid @RequestBody MaterialPriceRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found"));
        MaterialPriceHistory history = new MaterialPriceHistory();
        history.setProduct(product);
        history.setPreviousPurchasePrice(product.getPurchasePrice());
        history.setPreviousSalesPrice(product.getSalesPrice());
        history.setPreviousRetailerPrice(product.getRetailerPrice());
        history.setPreviousVatPercent(product.getVatPercent());
        history.setPreviousDiscountPercent(product.getDiscountPercent());
        history.setCurrentPurchasePrice(request.purchasePrice());
        history.setCurrentSalesPrice(request.salesPrice());
        history.setCurrentRetailerPrice(request.retailerPrice());
        history.setCurrentVatPercent(request.vatPercent() == null ? BigDecimal.ZERO : request.vatPercent());
        history.setCurrentDiscountPercent(request.discountPercent() == null ? BigDecimal.ZERO : request.discountPercent());
        history.setRemarks(request.remarks());
        product.setPurchasePrice(request.purchasePrice());
        product.setSalesPrice(request.salesPrice());
        product.setRetailerPrice(request.retailerPrice());
        product.setVatPercent(request.vatPercent() == null ? BigDecimal.ZERO : request.vatPercent());
        product.setDiscountPercent(request.discountPercent() == null ? BigDecimal.ZERO : request.discountPercent());
        Product saved = productRepository.save(product);
        priceHistoryRepository.save(history);
        return ApiResponse.ok("Material price updated", ProductMapper.toDto(saved));
    }

    @GetMapping("/movements")
    @Transactional(readOnly = true)
    public ApiResponse<List<StockMovementResponse>> movements(@RequestParam(required = false) Long productId,
                                                              @RequestParam(required = false) String movementTypeCode) {
        String code = movementTypeCode == null || movementTypeCode.isBlank() ? null : movementTypeCode;
        return ApiResponse.ok("Material movements loaded", stockRepository.movementHistory(productId, code).stream()
                .map(this::toMovementResponse)
                .toList());
    }

    @GetMapping("/price-history")
    @Transactional(readOnly = true)
    public ApiResponse<List<MaterialPriceHistoryResponse>> priceHistory(@RequestParam(required = false) Long productId) {
        List<MaterialPriceHistory> rows = productId == null
                ? priceHistoryRepository.findTop100ByOrderByPostingDateDescIdDesc()
                : priceHistoryRepository.findTop100ByProductIdOrderByPostingDateDescIdDesc(productId);
        return ApiResponse.ok("Material price history loaded", rows.stream().map(this::toPriceHistoryResponse).toList());
    }

    private MaterialStockSummary toStockSummary(Product product, Long warehouseId) {
        return new MaterialStockSummary(
                product.getId(),
                product.getProductCode(),
                product.getProductName(),
                product.getMaterialType(),
                product.getUnit() == null ? null : product.getUnit().getId(),
                product.getPackSize(),
                product.getPurchasePrice(),
                product.getSalesPrice(),
                product.getRetailerPrice(),
                product.getVatPercent(),
                product.getDiscountPercent(),
                product.getReorderLevel(),
                warehouseId,
                stockRepository.currentStock(product.getId(), warehouseId)
        );
    }

    private StockMovementResponse toMovementResponse(StockTransaction tx) {
        return new StockMovementResponse(
                tx.getId(),
                tx.getProduct().getId(),
                tx.getProduct().getProductCode(),
                tx.getProduct().getProductName(),
                tx.getWarehouse().getId(),
                tx.getWarehouse().getName(),
                tx.getTransactionType(),
                tx.getMovementTypeCode(),
                tx.getMovementTypeName(),
                tx.getQuantity(),
                tx.getBatchNumber(),
                tx.getExpiryDate(),
                tx.getReferenceType(),
                tx.getReferenceId(),
                tx.getRemarks(),
                tx.getCreatedAt()
        );
    }

    private MaterialPriceHistoryResponse toPriceHistoryResponse(MaterialPriceHistory history) {
        Product product = history.getProduct();
        return new MaterialPriceHistoryResponse(
                history.getId(),
                product.getId(),
                product.getProductCode(),
                product.getProductName(),
                history.getPostingDate(),
                history.getPreviousPurchasePrice(),
                history.getCurrentPurchasePrice(),
                history.getPreviousSalesPrice(),
                history.getCurrentSalesPrice(),
                history.getPreviousRetailerPrice(),
                history.getCurrentRetailerPrice(),
                history.getPreviousVatPercent(),
                history.getCurrentVatPercent(),
                history.getPreviousDiscountPercent(),
                history.getCurrentDiscountPercent(),
                history.getRemarks(),
                history.getCreatedAt()
        );
    }
}
