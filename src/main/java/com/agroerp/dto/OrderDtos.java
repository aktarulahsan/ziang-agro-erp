package com.agroerp.dto;

import com.agroerp.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class OrderDtos {
    private OrderDtos() {}

    public record OrderItemRequest(@NotNull Long productId, @NotNull @Positive BigDecimal quantity) {}
    public record OrderRequest(@NotNull Long retailerId, @Valid @NotEmpty List<OrderItemRequest> items) {}
    public record OrderItemResponse(Long productId, String productName, BigDecimal quantity, BigDecimal unitPrice,
                                    BigDecimal discountAmount, BigDecimal vatAmount, BigDecimal lineTotal) {}
    public record OrderResponse(Long id, String orderNumber, LocalDate orderDate, Long retailerId, String retailerName,
                                OrderStatus status, BigDecimal grossAmount, BigDecimal discountAmount,
                                BigDecimal vatAmount, BigDecimal netAmount, List<OrderItemResponse> items) {}
}
