package com.agroerp.service;

import com.agroerp.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDto create(ProductDto dto);
    ProductDto update(Long id, ProductDto dto);
    ProductDto get(Long id);
    Page<ProductDto> search(String keyword, Pageable pageable);
    void softDelete(Long id);
}
