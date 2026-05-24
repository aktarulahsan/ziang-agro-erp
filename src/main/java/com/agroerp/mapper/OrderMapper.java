package com.agroerp.mapper;

import com.agroerp.dto.OrderDtos.OrderItemResponse;
import com.agroerp.dto.OrderDtos.OrderResponse;
import com.agroerp.entity.Order;

public final class OrderMapper {
    private OrderMapper() {}

    public static OrderResponse toDto(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getOrderDate(),
                order.getRetailer().getId(),
                order.getRetailer().getRetailerName(),
                order.getStatus(),
                order.getGrossAmount(),
                order.getDiscountAmount(),
                order.getVatAmount(),
                order.getNetAmount(),
                order.getItems().stream()
                        .map(i -> new OrderItemResponse(i.getProduct().getId(), i.getProduct().getProductName(),
                                i.getQuantity(), i.getUnitPrice(), i.getDiscountAmount(), i.getVatAmount(), i.getLineTotal()))
                        .toList()
        );
    }
}
