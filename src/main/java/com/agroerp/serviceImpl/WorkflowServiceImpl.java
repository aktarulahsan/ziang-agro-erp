package com.agroerp.serviceImpl;

import com.agroerp.dto.WorkflowDtos.DeliveryRequest;
import com.agroerp.dto.WorkflowDtos.DeliveryResponse;
import com.agroerp.dto.WorkflowDtos.InvoiceResponse;
import com.agroerp.dto.WorkflowDtos.PaymentRequest;
import com.agroerp.dto.WorkflowDtos.PaymentResponse;
import com.agroerp.entity.*;
import com.agroerp.enums.*;
import com.agroerp.exception.BusinessException;
import com.agroerp.exception.ResourceNotFoundException;
import com.agroerp.repository.*;
import com.agroerp.service.AuditService;
import com.agroerp.service.AccountService;
import com.agroerp.service.LedgerService;
import com.agroerp.service.StockService;
import com.agroerp.service.WorkflowService;
import com.agroerp.util.NumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class WorkflowServiceImpl implements WorkflowService {
    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;
    private final RetailerRepository retailerRepository;
    private final StockService stockService;
    private final LedgerService ledgerService;
    private final AccountService accountService;
    private final NumberGenerator numberGenerator;
    private final AuditService auditService;

    public WorkflowServiceImpl(OrderRepository orderRepository, InvoiceRepository invoiceRepository,
                               DeliveryRepository deliveryRepository, PaymentRepository paymentRepository,
                               RetailerRepository retailerRepository, StockService stockService,
                               LedgerService ledgerService, AccountService accountService,
                               NumberGenerator numberGenerator, AuditService auditService) {
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.deliveryRepository = deliveryRepository;
        this.paymentRepository = paymentRepository;
        this.retailerRepository = retailerRepository;
        this.stockService = stockService;
        this.ledgerService = ledgerService;
        this.accountService = accountService;
        this.numberGenerator = numberGenerator;
        this.auditService = auditService;
    }

    @Override
    @Transactional
    public InvoiceResponse generateInvoice(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new BusinessException("Only approved orders can be invoiced");
        }
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(numberGenerator.nextFromDatabase("INV",
                invoiceRepository::findMaxInvoiceNumberForPrefix,
                invoiceRepository::existsByInvoiceNumber));
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setRetailer(order.getRetailer());
        invoice.setOrder(order);
        invoice.setNetAmount(order.getNetAmount());
        invoice.setDueAmount(order.getNetAmount());
        for (OrderItem orderItem : order.getItems()) {
            InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            item.setProduct(orderItem.getProduct());
            item.setQuantity(orderItem.getQuantity());
            item.setUnitPrice(orderItem.getUnitPrice());
            item.setLineTotal(orderItem.getLineTotal());
            invoice.getItems().add(item);
        }
        Invoice saved = invoiceRepository.save(invoice);
        order.setStatus(OrderStatus.INVOICED);
        ledgerService.post(order.getRetailer(), saved.getInvoiceDate(), LedgerType.INVOICE, saved.getNetAmount(),
                BigDecimal.ZERO, "INVOICE", saved.getId(), "Invoice " + saved.getInvoiceNumber());
        accountService.post("1100", saved.getInvoiceDate(), saved.getNetAmount(), BigDecimal.ZERO,
                "INVOICE", saved.getId(), "Accounts receivable for " + saved.getInvoiceNumber());
        accountService.post("4000", saved.getInvoiceDate(), BigDecimal.ZERO, saved.getNetAmount(),
                "INVOICE", saved.getId(), "Sales revenue for " + saved.getInvoiceNumber());
        auditService.log("Invoice", "GENERATE", saved.getId(), saved.getInvoiceNumber());
        return toInvoiceResponse(saved);
    }

    @Override
    @Transactional
    public DeliveryResponse createDelivery(Long invoiceId, DeliveryRequest request) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new BusinessException("Cancelled invoice cannot be delivered");
        }
        if (deliveryRepository.existsByInvoiceIdAndStatusNot(invoiceId, DeliveryStatus.CANCELLED)) {
            throw new BusinessException("Delivery already exists for this invoice");
        }
        Delivery delivery = new Delivery();
        delivery.setDeliveryNumber(numberGenerator.nextFromDatabase("DLV",
                deliveryRepository::findMaxDeliveryNumberForPrefix,
                deliveryRepository::existsByDeliveryNumber));
        delivery.setDeliveryDate(LocalDate.now());
        delivery.setInvoice(invoice);
        delivery.setRetailer(invoice.getRetailer());
        delivery.setDeliveryAddress(request.deliveryAddress());
        delivery.setVehicleDetails(request.vehicleDetails());
        delivery.setDeliveryPerson(request.deliveryPerson());
        delivery.setDeliveryContactNo(request.deliveryContactNo());
        delivery.setRouteName(request.routeName());
        delivery.setTransportCost(request.transportCost() == null ? BigDecimal.ZERO : request.transportCost());
        delivery.setRemarks(request.remarks());
        delivery.setStatus(DeliveryStatus.DELIVERED);
        for (InvoiceItem invoiceItem : invoice.getItems()) {
            stockService.ensureAvailable(invoiceItem.getProduct().getId(), request.warehouseId(), invoiceItem.getQuantity());
            DeliveryItem item = new DeliveryItem();
            item.setDelivery(delivery);
            item.setProduct(invoiceItem.getProduct());
            item.setDeliveredQuantity(invoiceItem.getQuantity());
            item.setPendingQuantity(BigDecimal.ZERO);
            delivery.getItems().add(item);
        }
        Delivery saved = deliveryRepository.save(delivery);
        for (DeliveryItem item : saved.getItems()) {
            stockService.stockOut(item.getProduct().getId(), request.warehouseId(), item.getDeliveredQuantity(),
                    StockTransactionType.SALES_OUT, "DELIVERY", saved.getId());
        }
        invoice.getOrder().setStatus(OrderStatus.DELIVERED);
        auditService.log("Delivery", "CREATE", saved.getId(), saved.getDeliveryNumber());
        return new DeliveryResponse(saved.getId(), saved.getDeliveryNumber(), saved.getDeliveryDate(), invoiceId,
                invoice.getInvoiceNumber(), invoice.getRetailer().getId(), invoice.getRetailer().getRetailerName(),
                saved.getDeliveryPerson(), saved.getVehicleDetails(), saved.getStatus());
    }

    @Override
    @Transactional
    public PaymentResponse receivePayment(PaymentRequest request) {
        Retailer retailer = retailerRepository.findById(request.retailerId()).orElseThrow(() -> new ResourceNotFoundException("Retailer not found"));
        Invoice invoice = null;
        if (request.invoiceId() != null) {
            invoice = invoiceRepository.findById(request.invoiceId()).orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
            BigDecimal paid = invoice.getPaidAmount().add(request.amount());
            invoice.setPaidAmount(paid);
            invoice.setDueAmount(invoice.getNetAmount().subtract(paid).max(BigDecimal.ZERO));
            invoice.setStatus(invoice.getDueAmount().compareTo(BigDecimal.ZERO) == 0 ? InvoiceStatus.PAID : InvoiceStatus.PARTIALLY_PAID);
        }
        Payment payment = new Payment();
        payment.setReceiptNumber(numberGenerator.nextFromDatabase("MR",
                paymentRepository::findMaxReceiptNumberForPrefix,
                paymentRepository::existsByReceiptNumber));
        payment.setPaymentDate(LocalDate.now());
        payment.setRetailer(retailer);
        payment.setInvoice(invoice);
        payment.setPaymentMethod(request.paymentMethod());
        payment.setAmount(request.amount());
        payment.setReferenceNumber(request.referenceNumber());
        payment.setRemarks(request.remarks());
        payment.setApproved(true);
        Payment saved = paymentRepository.save(payment);
        ledgerService.post(retailer, saved.getPaymentDate(), LedgerType.PAYMENT, BigDecimal.ZERO, saved.getAmount(),
                "PAYMENT", saved.getId(), "Money receipt " + saved.getReceiptNumber());
        accountService.post("1000", saved.getPaymentDate(), saved.getAmount(), BigDecimal.ZERO,
                "PAYMENT", saved.getId(), "Cash received " + saved.getReceiptNumber());
        accountService.post("1100", saved.getPaymentDate(), BigDecimal.ZERO, saved.getAmount(),
                "PAYMENT", saved.getId(), "Receivable settled " + saved.getReceiptNumber());
        auditService.log("Payment", "RECEIVE", saved.getId(), saved.getReceiptNumber());
        return new PaymentResponse(saved.getId(), saved.getReceiptNumber(), saved.getPaymentDate(), retailer.getId(),
                invoice == null ? null : invoice.getId(), saved.getPaymentMethod(), saved.getAmount(), saved.isApproved());
    }

    private InvoiceResponse toInvoiceResponse(Invoice invoice) {
        return new InvoiceResponse(invoice.getId(), invoice.getInvoiceNumber(), invoice.getInvoiceDate(),
                invoice.getRetailer().getId(), invoice.getRetailer().getRetailerName(),
                invoice.getOrder().getId(), invoice.getOrder().getOrderNumber(), invoice.getStatus(),
                invoice.getNetAmount(), invoice.getPaidAmount(), invoice.getDueAmount());
    }
}
