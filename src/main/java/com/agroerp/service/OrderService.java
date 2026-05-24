package com.agroerp.service;

import com.agroerp.dto.OrderDtos.OrderRequest;
import com.agroerp.dto.OrderDtos.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse create(OrderRequest request);
    OrderResponse approve(Long orderId);
    OrderResponse get(Long orderId);
    Page<OrderResponse> list(Pageable pageable);
}
