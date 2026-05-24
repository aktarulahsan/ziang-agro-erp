package com.agroerp.service;

import com.agroerp.dto.RetailerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RetailerService {
    RetailerDto create(RetailerDto dto);
    RetailerDto update(Long id, RetailerDto dto);
    RetailerDto get(Long id);
    Page<RetailerDto> search(String keyword, Pageable pageable);
    void softDelete(Long id);
}
