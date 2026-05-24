package com.agroerp.serviceImpl;

import com.agroerp.dto.ProductPriceDtos.*;
import com.agroerp.entity.AccountingEntry;
import com.agroerp.entity.PriceChangeHistory;
import com.agroerp.entity.Product;
import com.agroerp.enums.AccountingEntryType;
import com.agroerp.enums.PriceConditionType;
import com.agroerp.exception.BusinessException;
import com.agroerp.repository.AccountingEntryRepository;
import com.agroerp.repository.PriceChangeHistoryRepository;
import com.agroerp.repository.ProductRepository;
import com.agroerp.repository.StockTransactionRepository;
import com.agroerp.service.ProductPriceService;
import com.agroerp.util.NumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProductPriceServiceImpl implements ProductPriceService {
    private final ProductRepository productRepository;
    private final StockTransactionRepository stockRepository;
    private final PriceChangeHistoryRepository historyRepository;
    private final AccountingEntryRepository accountingEntryRepository;
    private final NumberGenerator numberGenerator;

    public ProductPriceServiceImpl(ProductRepository productRepository, StockTransactionRepository stockRepository,
                                   PriceChangeHistoryRepository historyRepository,
                                   AccountingEntryRepository accountingEntryRepository, NumberGenerator numberGenerator) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.historyRepository = historyRepository;
        this.accountingEntryRepository = accountingEntryRepository;
        this.numberGenerator = numberGenerator;
    }

    @Override
    @Transactional
    public ProductPriceUpdateResponse updatePrice(String productCode, ProductPriceUpdateRequest request) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new BusinessException("Product not found: " + productCode));
        BigDecimal oldPrice = currentPrice(product);
        BigDecimal effectiveNewPrice = applyConditions(request.newPrice(), request.conditions());
        BigDecimal totalStock = stockRepository.currentStockAllWarehouses(product.getId());
        BigDecimal beforeValue = oldPrice.multiply(totalStock);
        BigDecimal afterValue = effectiveNewPrice.multiply(totalStock);
        BigDecimal inventoryValueChange = afterValue.subtract(beforeValue);
        String materialDocumentNumber = numberGenerator.next("PRC");

        product.setCurrentPrice(effectiveNewPrice);
        product.setRetailerPrice(effectiveNewPrice);
        product.setTotalStockQuantity(totalStock);
        product.setCurrentInventoryValue(afterValue);
        productRepository.save(product);

        PriceChangeHistory history = new PriceChangeHistory();
        history.setProduct(product);
        history.setOldPrice(oldPrice);
        history.setNewPrice(effectiveNewPrice);
        history.setRequestedPrice(request.newPrice());
        history.setChangeReason(request.reason());
        history.setChangedBy(request.changedBy());
        history.setChangeDateTime(Instant.now());
        history.setInventoryValueBefore(beforeValue);
        history.setInventoryValueAfter(afterValue);
        history.setInventoryValueChange(inventoryValueChange);
        history.setConditionSummary(conditionSummary(request.conditions()));
        history.setMaterialDocumentNumber(materialDocumentNumber);
        historyRepository.save(history);

        createAccountingEntries(product, inventoryValueChange, materialDocumentNumber);

        return new ProductPriceUpdateResponse("Price updated successfully", oldPrice, effectiveNewPrice, request.newPrice(),
                totalStock, beforeValue, afterValue, inventoryValueChange, materialDocumentNumber);
    }

    @Override
    @Transactional
    public List<ProductPriceUpdateResponse> bulkUpdate(BulkProductPriceUpdateRequest request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new BusinessException("At least one product price item is required");
        }
        return request.items().stream()
                .map(item -> updatePrice(item.productCode(), new ProductPriceUpdateRequest(
                        item.newPrice(), item.reason(), item.changedBy(), item.conditions())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceChangeHistoryResponse> history(String productCode) {
        return historyRepository.findByProductProductCodeOrderByChangeDateTimeDescIdDesc(productCode).stream()
                .map(this::toHistoryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountingEntryResponse> accountingEntries(String productCode) {
        return accountingEntryRepository.findByProductProductCodeOrderByPostingDateDescIdDesc(productCode).stream()
                .map(this::toAccountingResponse)
                .toList();
    }

    private BigDecimal currentPrice(Product product) {
        if (product.getCurrentPrice() != null && product.getCurrentPrice().compareTo(BigDecimal.ZERO) > 0) {
            return product.getCurrentPrice();
        }
        if (product.getRetailerPrice() != null && product.getRetailerPrice().compareTo(BigDecimal.ZERO) > 0) {
            return product.getRetailerPrice();
        }
        return product.getSalesPrice() == null ? BigDecimal.ZERO : product.getSalesPrice();
    }

    private BigDecimal applyConditions(BigDecimal basePrice, List<PriceConditionRequest> conditions) {
        BigDecimal price = basePrice;
        if (conditions == null) return price;
        for (PriceConditionRequest condition : conditions) {
            if (condition.conditionType() == PriceConditionType.DISCOUNT) {
                price = price.subtract(condition.amount());
            } else if (condition.conditionType() == PriceConditionType.SURCHARGE) {
                price = price.add(condition.amount());
            }
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Effective price cannot be negative");
        }
        return price;
    }

    private String conditionSummary(List<PriceConditionRequest> conditions) {
        if (conditions == null || conditions.isEmpty()) return "";
        return conditions.stream()
                .map(c -> c.conditionType() + ":" + c.amount() + (c.description() == null ? "" : ":" + c.description()))
                .toList()
                .toString();
    }

    private void createAccountingEntries(Product product, BigDecimal inventoryValueChange, String materialDocumentNumber) {
        if (inventoryValueChange.compareTo(BigDecimal.ZERO) == 0) return;
        BigDecimal amount = inventoryValueChange.abs();
        if (inventoryValueChange.compareTo(BigDecimal.ZERO) > 0) {
            saveEntry(product, AccountingEntryType.DEBIT, amount, "Inventory Asset debit for price increase", materialDocumentNumber);
            saveEntry(product, AccountingEntryType.CREDIT, amount, "Price Variance credit for price increase", materialDocumentNumber);
        } else {
            saveEntry(product, AccountingEntryType.CREDIT, amount, "Inventory Asset credit for price decrease", materialDocumentNumber);
            saveEntry(product, AccountingEntryType.DEBIT, amount, "Price Variance debit for price decrease", materialDocumentNumber);
        }
    }

    private void saveEntry(Product product, AccountingEntryType type, BigDecimal amount, String description, String materialDocumentNumber) {
        AccountingEntry entry = new AccountingEntry();
        entry.setProduct(product);
        entry.setEntryType(type);
        entry.setAmount(amount);
        entry.setDescription(description);
        entry.setPostingDate(LocalDate.now());
        entry.setMaterialDocumentNumber(materialDocumentNumber);
        accountingEntryRepository.save(entry);
    }

    private PriceChangeHistoryResponse toHistoryResponse(PriceChangeHistory history) {
        Product product = history.getProduct();
        return new PriceChangeHistoryResponse(history.getId(), product.getProductCode(), product.getProductName(),
                history.getOldPrice(), history.getNewPrice(), history.getRequestedPrice(), history.getChangeReason(),
                history.getChangedBy(), history.getChangeDateTime(), history.getInventoryValueBefore(),
                history.getInventoryValueAfter(), history.getInventoryValueChange(), history.getConditionSummary(),
                history.getMaterialDocumentNumber());
    }

    private AccountingEntryResponse toAccountingResponse(AccountingEntry entry) {
        Product product = entry.getProduct();
        return new AccountingEntryResponse(entry.getId(), product.getProductCode(), product.getProductName(),
                entry.getEntryType(), entry.getAmount(), entry.getDescription(), entry.getPostingDate(),
                entry.getMaterialDocumentNumber());
    }
}
