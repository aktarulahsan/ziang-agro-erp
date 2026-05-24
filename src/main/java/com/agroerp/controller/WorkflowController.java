package com.agroerp.controller;

import com.agroerp.dto.WorkflowDtos.DeliveryRequest;
import com.agroerp.dto.WorkflowDtos.DeliveryResponse;
import com.agroerp.dto.WorkflowDtos.InvoiceResponse;
import com.agroerp.dto.WorkflowDtos.PaymentRequest;
import com.agroerp.dto.WorkflowDtos.PaymentResponse;
import com.agroerp.response.ApiResponse;
import com.agroerp.service.WorkflowService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {
    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/orders/{orderId}/invoice")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','ACCOUNTS_USER')")
    public ApiResponse<InvoiceResponse> invoice(@PathVariable Long orderId) {
        return ApiResponse.ok("Invoice generated", workflowService.generateInvoice(orderId));
    }

    @PostMapping("/invoices/{invoiceId}/delivery")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','STORE_USER','DELIVERY_USER')")
    public ApiResponse<DeliveryResponse> delivery(@PathVariable Long invoiceId, @Valid @RequestBody DeliveryRequest request) {
        return ApiResponse.ok("Delivery created", workflowService.createDelivery(invoiceId, request));
    }

    @PostMapping("/payments")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','ACCOUNTS_USER','SALES_MANAGER')")
    public ApiResponse<PaymentResponse> payment(@Valid @RequestBody PaymentRequest request) {
        return ApiResponse.ok("Payment received", workflowService.receivePayment(request));
    }
}
