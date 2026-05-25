package com.agroerp.controller;

import com.agroerp.dto.WorkflowDtos.InvoicePrintResponse;
import com.agroerp.dto.WorkflowDtos.InvoiceResponse;
import com.agroerp.dto.WorkflowDtos.PrintItemResponse;
import com.agroerp.entity.Invoice;
import com.agroerp.entity.InvoiceItem;
import com.agroerp.enums.DeliveryStatus;
import com.agroerp.enums.InvoiceStatus;
import com.agroerp.repository.DeliveryRepository;
import com.agroerp.repository.InvoiceRepository;
import com.agroerp.response.ApiResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceRepository invoiceRepository;
    private final DeliveryRepository deliveryRepository;

    public InvoiceController(InvoiceRepository invoiceRepository, DeliveryRepository deliveryRepository) {
        this.invoiceRepository = invoiceRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ApiResponse<List<InvoiceResponse>> list() {
        return ApiResponse.ok("Invoices loaded", invoiceRepository.findAll().stream().map(this::toDto).toList());
    }

    @GetMapping("/delivery-pending")
    @Transactional(readOnly = true)
    public ApiResponse<List<InvoiceResponse>> deliveryPending() {
        var deliveredInvoiceIds = deliveryRepository.findByStatusNot(DeliveryStatus.CANCELLED).stream()
                .map(delivery -> delivery.getInvoice().getId())
                .collect(java.util.stream.Collectors.toSet());
        var invoices = invoiceRepository.findAll().stream()
                .filter(invoice -> invoice.getStatus() != InvoiceStatus.CANCELLED)
                .filter(invoice -> !deliveredInvoiceIds.contains(invoice.getId()))
                .map(this::toDto)
                .toList();
        return ApiResponse.ok("Delivery pending invoices loaded", invoices);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ApiResponse<InvoiceResponse> get(@PathVariable Long id) {
        return ApiResponse.ok("Invoice loaded", invoiceRepository.findById(id).map(this::toDto).orElseThrow());
    }

    @GetMapping("/{id}/print")
    @Transactional(readOnly = true)
    public ApiResponse<InvoicePrintResponse> print(@PathVariable Long id) {
        return ApiResponse.ok("Invoice print loaded", invoiceRepository.findPrintById(id).map(this::toPrintDto).orElseThrow());
    }

    private InvoiceResponse toDto(Invoice invoice) {
        return new InvoiceResponse(invoice.getId(), invoice.getInvoiceNumber(), invoice.getInvoiceDate(),
                invoice.getRetailer().getId(), invoice.getRetailer().getRetailerName(),
                invoice.getOrder().getId(), invoice.getOrder().getOrderNumber(), invoice.getStatus(),
                invoice.getNetAmount(), invoice.getPaidAmount(), invoice.getDueAmount());
    }

    private InvoicePrintResponse toPrintDto(Invoice invoice) {
        var retailer = invoice.getRetailer();
        var order = invoice.getOrder();
        return new InvoicePrintResponse(invoice.getId(), invoice.getInvoiceNumber(), invoice.getInvoiceDate(), invoice.getStatus(),
                retailer.getId(), retailer.getRetailerCode(), retailer.getRetailerName(), retailer.getOwnerName(),
                retailer.getMobileNumber(), retailer.getAddress(), retailer.getMarketName(),
                order.getId(), order.getOrderNumber(), order.getOrderDate(),
                order.getGrossAmount(), order.getDiscountAmount(), order.getVatAmount(),
                invoice.getNetAmount(), invoice.getPaidAmount(), invoice.getDueAmount(),
                invoice.getItems().stream().map(this::toPrintItem).toList());
    }

    private PrintItemResponse toPrintItem(InvoiceItem item) {
        var product = item.getProduct();
        return new PrintItemResponse(product.getId(), product.getProductCode(), product.getProductName(),
                item.getQuantity(), item.getUnitPrice(), java.math.BigDecimal.ZERO,
                java.math.BigDecimal.ZERO, item.getLineTotal());
    }
}
