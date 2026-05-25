package com.agroerp.dto;

import com.agroerp.enums.DeliveryStatus;
import com.agroerp.enums.InvoiceStatus;
import com.agroerp.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class WorkflowDtos {
    private WorkflowDtos() {}

    public record InvoiceResponse(Long id, String invoiceNumber, LocalDate invoiceDate, Long retailerId,
                                  String retailerName, Long orderId, String orderNumber, InvoiceStatus status,
                                  BigDecimal netAmount, BigDecimal paidAmount, BigDecimal dueAmount) {}

    public record PrintItemResponse(Long productId, String productCode, String productName,
                                    BigDecimal quantity, BigDecimal unitPrice, BigDecimal discountAmount,
                                    BigDecimal vatAmount, BigDecimal lineTotal) {}

    public record InvoicePrintResponse(Long id, String invoiceNumber, LocalDate invoiceDate, InvoiceStatus status,
                                       Long retailerId, String retailerCode, String retailerName, String ownerName,
                                       String mobileNumber, String address, String marketName,
                                       Long orderId, String orderNumber, LocalDate orderDate,
                                       BigDecimal grossAmount, BigDecimal discountAmount, BigDecimal vatAmount,
                                       BigDecimal netAmount, BigDecimal paidAmount, BigDecimal dueAmount,
                                       java.util.List<PrintItemResponse> items) {}

    public record DeliveryRequest(String deliveryAddress, String vehicleDetails, String deliveryPerson,
                                  String deliveryContactNo, String routeName, BigDecimal transportCost,
                                  String remarks, @NotNull Long warehouseId) {}
    public record DeliveryResponse(Long id, String deliveryNumber, LocalDate deliveryDate, Long invoiceId,
                                   String invoiceNumber, Long retailerId, String retailerName,
                                   String deliveryPerson, String vehicleDetails, DeliveryStatus status) {}

    public record DeliveryPrintResponse(Long id, String deliveryNumber, LocalDate deliveryDate, DeliveryStatus status,
                                        Long invoiceId, String invoiceNumber, LocalDate invoiceDate,
                                        Long retailerId, String retailerCode, String retailerName, String ownerName,
                                        String mobileNumber, String address, String deliveryAddress,
                                        String vehicleDetails, String deliveryPerson, String deliveryContactNo,
                                        String routeName, BigDecimal transportCost, String remarks,
                                        java.util.List<PrintItemResponse> items) {}

    public record PaymentRequest(@NotNull Long retailerId, Long invoiceId, @NotNull PaymentMethod paymentMethod,
                                 @NotNull @Positive BigDecimal amount, String referenceNumber, String remarks) {}
    public record PaymentResponse(Long id, String receiptNumber, LocalDate paymentDate, Long retailerId,
                                  Long invoiceId, PaymentMethod paymentMethod, BigDecimal amount, boolean approved) {}
}
