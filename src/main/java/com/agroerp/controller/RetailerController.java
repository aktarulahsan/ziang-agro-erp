package com.agroerp.controller;

import com.agroerp.dto.RetailerDto;
import com.agroerp.response.ApiResponse;
import com.agroerp.service.RetailerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/retailers")
public class RetailerController {
    private final RetailerService retailerService;

    public RetailerController(RetailerService retailerService) {
        this.retailerService = retailerService;
    }

    @GetMapping
    public ApiResponse<Page<RetailerDto>> list(@RequestParam(required = false) String q, Pageable pageable) {
        return ApiResponse.ok("Retailers loaded", retailerService.search(q, pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<RetailerDto> get(@PathVariable Long id) {
        return ApiResponse.ok("Retailer loaded", retailerService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SALES_MANAGER')")
    public ApiResponse<RetailerDto> create(@Valid @RequestBody RetailerDto dto) {
        return ApiResponse.ok("Retailer created", retailerService.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SALES_MANAGER')")
    public ApiResponse<RetailerDto> update(@PathVariable Long id, @Valid @RequestBody RetailerDto dto) {
        return ApiResponse.ok("Retailer updated", retailerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        retailerService.softDelete(id);
        return ApiResponse.ok("Retailer deleted", null);
    }
}
