package com.agroerp.controller;

import com.agroerp.dto.OrderDtos.OrderRequest;
import com.agroerp.dto.OrderDtos.OrderResponse;
import com.agroerp.response.ApiResponse;
import com.agroerp.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SALES_MANAGER','SALES_OFFICER','RETAILER')")
    public ApiResponse<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        return ApiResponse.ok("Order created", orderService.create(request));
    }

    @GetMapping
    public ApiResponse<Page<OrderResponse>> list(Pageable pageable) {
        return ApiResponse.ok("Orders loaded", orderService.list(pageable));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SALES_MANAGER')")
    public ApiResponse<OrderResponse> approve(@PathVariable Long id) {
        return ApiResponse.ok("Order approved", orderService.approve(id));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> get(@PathVariable Long id) {
        return ApiResponse.ok("Order loaded", orderService.get(id));
    }
}
