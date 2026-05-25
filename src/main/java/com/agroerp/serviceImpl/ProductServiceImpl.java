package com.agroerp.serviceImpl;

import com.agroerp.dto.ProductDto;
import com.agroerp.entity.Product;
import com.agroerp.enums.MaterialType;
import com.agroerp.exception.BusinessException;
import com.agroerp.exception.ResourceNotFoundException;
import com.agroerp.mapper.ProductMapper;
import com.agroerp.repository.BrandRepository;
import com.agroerp.repository.CategoryRepository;
import com.agroerp.repository.ProductRepository;
import com.agroerp.repository.SubCategoryRepository;
import com.agroerp.repository.UnitRepository;
import com.agroerp.service.AuditService;
import com.agroerp.service.ProductService;
import com.agroerp.util.NumberGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;
    private final UnitRepository unitRepository;
    private final AuditService auditService;
    private final NumberGenerator numberGenerator;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                              SubCategoryRepository subCategoryRepository, BrandRepository brandRepository,
                              UnitRepository unitRepository, AuditService auditService, NumberGenerator numberGenerator) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.brandRepository = brandRepository;
        this.unitRepository = unitRepository;
        this.auditService = auditService;
        this.numberGenerator = numberGenerator;
    }

    @Override
    @Transactional
    public ProductDto create(ProductDto dto) {
        MaterialType materialType = dto.materialType() == null ? MaterialType.FINISHED_PRODUCTS : dto.materialType();
        String productCode = clean(dto.productCode());
        if (productCode == null) {
            productCode = nextProductCode(materialType);
        }
        if (productRepository.existsByProductCode(productCode)) {
            throw new BusinessException("Product code already exists");
        }
        Product product = new Product();
        apply(dto, product);
        product.setProductCode(productCode);
        product.setMaterialType(materialType);
        Product saved = productRepository.save(product);
        auditService.log("Product", "CREATE", saved.getId(), saved.getProductCode());
        return ProductMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ProductDto update(Long id, ProductDto dto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        apply(dto, product);
        auditService.log("Product", "UPDATE", id, dto.productCode());
        return ProductMapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductDto get(Long id) {
        return ProductMapper.toDto(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
    }

    @Override
    public Page<ProductDto> search(String keyword, Pageable pageable) {
        String q = keyword == null ? "" : keyword;
        return productRepository
                .findByDeletedFalseAndProductNameContainingIgnoreCaseOrDeletedFalseAndProductCodeContainingIgnoreCase(q, q, pageable)
                .map(ProductMapper::toDto);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setDeleted(true);
        product.setActive(false);
        auditService.log("Product", "DELETE", id, product.getProductCode());
    }

    private void apply(ProductDto dto, Product product) {
        if (clean(dto.productCode()) != null) {
            product.setProductCode(clean(dto.productCode()));
        }
        product.setProductName(dto.productName());
        product.setMaterialType(dto.materialType() == null ? MaterialType.FINISHED_PRODUCTS : dto.materialType());
        product.setPackSize(dto.packSize());
        product.setPurchasePrice(dto.purchasePrice());
        product.setSalesPrice(dto.salesPrice());
        product.setRetailerPrice(dto.retailerPrice());
        product.setCurrentPrice(dto.retailerPrice() == null ? BigDecimal.ZERO : dto.retailerPrice());
        product.setVatPercent(dto.vatPercent() == null ? BigDecimal.ZERO : dto.vatPercent());
        product.setDiscountPercent(dto.discountPercent() == null ? BigDecimal.ZERO : dto.discountPercent());
        product.setBatchNumber(dto.batchNumber());
        product.setExpiryDate(dto.expiryDate());
        product.setImageUrl(dto.imageUrl());
        product.setReorderLevel(dto.reorderLevel() == null ? BigDecimal.ZERO : dto.reorderLevel());
        product.setActive(dto.active());
        if (dto.categoryId() != null) product.setCategory(categoryRepository.findById(dto.categoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        if (dto.subCategoryId() != null) product.setSubCategory(subCategoryRepository.findById(dto.subCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Sub category not found")));
        if (dto.brandId() != null) product.setBrand(brandRepository.findById(dto.brandId()).orElseThrow(() -> new ResourceNotFoundException("Brand not found")));
        if (dto.unitId() != null) product.setUnit(unitRepository.findById(dto.unitId()).orElseThrow(() -> new ResourceNotFoundException("Unit not found")));
    }

    @Override
    public String nextProductCode(MaterialType materialType) {
        String prefix = switch (materialType == null ? MaterialType.FINISHED_PRODUCTS : materialType) {
            case RAW_MATERIALS -> "RM";
            case SEMIFINISHED_PRODUCTS -> "SF";
            case FINISHED_PRODUCTS -> "FG";
            case TRADING_PRODUCT -> "TP";
        };
        return numberGenerator.nextFromDatabase(prefix,
                productRepository::findMaxProductCodeForPrefix,
                productRepository::existsByProductCode);
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
