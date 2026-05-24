package com.agroerp.service;

import com.agroerp.dto.ProductPriceDtos.*;

import java.util.List;

public interface ProductPriceService {
    ProductPriceUpdateResponse updatePrice(String productCode, ProductPriceUpdateRequest request);
    List<ProductPriceUpdateResponse> bulkUpdate(BulkProductPriceUpdateRequest request);
    List<PriceChangeHistoryResponse> history(String productCode);
    List<AccountingEntryResponse> accountingEntries(String productCode);
}
