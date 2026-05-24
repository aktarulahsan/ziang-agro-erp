package com.agroerp.service;

import com.agroerp.dto.WorkflowDtos.DeliveryRequest;
import com.agroerp.dto.WorkflowDtos.DeliveryResponse;
import com.agroerp.dto.WorkflowDtos.InvoiceResponse;
import com.agroerp.dto.WorkflowDtos.PaymentRequest;
import com.agroerp.dto.WorkflowDtos.PaymentResponse;

public interface WorkflowService {
    InvoiceResponse generateInvoice(Long orderId);
    DeliveryResponse createDelivery(Long invoiceId, DeliveryRequest request);
    PaymentResponse receivePayment(PaymentRequest request);
}
