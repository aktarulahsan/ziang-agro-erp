package com.agroerp.controller;

import com.agroerp.dto.ProductDto;
import com.agroerp.dto.ProductPriceDtos.*;
import com.agroerp.enums.MaterialType;
import com.agroerp.enums.PriceConditionType;
import com.agroerp.exception.BusinessException;
import com.agroerp.response.ApiResponse;
import com.agroerp.service.ProductPriceService;
import com.agroerp.service.ProductService;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final ProductPriceService productPriceService;

    public ProductController(ProductService productService, ProductPriceService productPriceService) {
        this.productService = productService;
        this.productPriceService = productPriceService;
    }

    @GetMapping
    public ApiResponse<Page<ProductDto>> list(@RequestParam(required = false) String q, Pageable pageable) {
        return ApiResponse.ok("Products loaded", productService.search(q, pageable));
    }

    @GetMapping("/next-code")
    public ApiResponse<String> nextCode(@RequestParam(required = false) MaterialType materialType) {
        return ApiResponse.ok("Product code generated", productService.nextProductCode(materialType));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDto> get(@PathVariable Long id) {
        return ApiResponse.ok("Product loaded", productService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','STORE_USER')")
    public ApiResponse<ProductDto> create(@Valid @RequestBody ProductDto dto) {
        return ApiResponse.ok("Product created", productService.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','STORE_USER')")
    public ApiResponse<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductDto dto) {
        return ApiResponse.ok("Product updated", productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.softDelete(id);
        return ApiResponse.ok("Product deleted", null);
    }

    @PutMapping("/{productCode}/price")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','STORE_USER')")
    public ApiResponse<ProductPriceUpdateResponse> updatePrice(@PathVariable String productCode,
                                                               @Valid @RequestBody ProductPriceUpdateRequest request) {
        return ApiResponse.ok("Price updated successfully", productPriceService.updatePrice(productCode, request));
    }

    @PostMapping("/prices/bulk")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','STORE_USER')")
    public ApiResponse<List<ProductPriceUpdateResponse>> bulkPriceUpdate(@Valid @RequestBody BulkProductPriceUpdateRequest request) {
        return ApiResponse.ok("Bulk price update completed", productPriceService.bulkUpdate(request));
    }

    @PostMapping(value = "/prices/bulk-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','STORE_USER')")
    public ApiResponse<List<ProductPriceUpdateResponse>> bulkPriceUpload(@RequestParam MultipartFile file,
                                                                         @RequestParam(defaultValue = "system") String changedBy)
            throws IOException {
        return ApiResponse.ok("Bulk price upload completed", productPriceService.bulkUpdate(readPriceWorkbook(file, changedBy)));
    }

    @GetMapping("/{productCode}/price-history")
    public ApiResponse<List<PriceChangeHistoryResponse>> priceHistory(@PathVariable String productCode) {
        return ApiResponse.ok("Price history loaded", productPriceService.history(productCode));
    }

    @GetMapping("/{productCode}/accounting-entries")
    public ApiResponse<List<AccountingEntryResponse>> accountingEntries(@PathVariable String productCode) {
        return ApiResponse.ok("Accounting entries loaded", productPriceService.accountingEntries(productCode));
    }

    private BulkProductPriceUpdateRequest readPriceWorkbook(MultipartFile file, String defaultChangedBy) throws IOException {
        if (file.isEmpty()) throw new BusinessException("Upload an Excel file with price rows.");
        DataFormatter formatter = new DataFormatter();
        List<BulkProductPriceUpdateItem> items = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            for (Row row : workbook.getSheetAt(0)) {
                String productCode = formatter.formatCellValue(row.getCell(0)).trim();
                if (productCode.isBlank() || productCode.equalsIgnoreCase("productCode")) continue;
                BigDecimal newPrice = decimal(formatter.formatCellValue(row.getCell(1)), "newPrice");
                String reason = text(formatter.formatCellValue(row.getCell(2)), "Bulk Excel price update");
                String rowChangedBy = text(formatter.formatCellValue(row.getCell(3)), defaultChangedBy);
                BigDecimal surcharge = decimalOrZero(formatter.formatCellValue(row.getCell(4)));
                BigDecimal discount = decimalOrZero(formatter.formatCellValue(row.getCell(5)));
                List<PriceConditionRequest> conditions = new ArrayList<>();
                if (surcharge.signum() > 0) {
                    conditions.add(new PriceConditionRequest(PriceConditionType.SURCHARGE, surcharge, "Excel surcharge condition"));
                }
                if (discount.signum() > 0) {
                    conditions.add(new PriceConditionRequest(PriceConditionType.DISCOUNT, discount, "Excel discount condition"));
                }
                items.add(new BulkProductPriceUpdateItem(productCode, newPrice, reason, rowChangedBy, conditions));
            }
        }
        if (items.isEmpty()) throw new BusinessException("No valid price rows found in Excel file.");
        return new BulkProductPriceUpdateRequest(items);
    }

    private String text(String value, String fallback) {
        return value == null || value.trim().isBlank() ? fallback : value.trim();
    }

    private BigDecimal decimal(String value, String fieldName) {
        try {
            return new BigDecimal(text(value, "0").replace(",", ""));
        } catch (NumberFormatException ex) {
            throw new BusinessException("Invalid " + fieldName + " value: " + value);
        }
    }

    private BigDecimal decimalOrZero(String value) {
        return value == null || value.trim().isBlank() ? BigDecimal.ZERO : decimal(value, "condition amount");
    }
}
