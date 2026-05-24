package com.agroerp.serviceImpl;

import com.agroerp.dto.DashboardSummaryDto;
import com.agroerp.enums.DeliveryStatus;
import com.agroerp.enums.InvoiceStatus;
import com.agroerp.repository.*;
import com.agroerp.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;
    private final RetailerRepository retailerRepository;
    private final ProductRepository productRepository;

    public DashboardServiceImpl(OrderRepository orderRepository, InvoiceRepository invoiceRepository,
                                DeliveryRepository deliveryRepository, PaymentRepository paymentRepository,
                                RetailerRepository retailerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.deliveryRepository = deliveryRepository;
        this.paymentRepository = paymentRepository;
        this.retailerRepository = retailerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public DashboardSummaryDto summary() {
        LocalDate today = LocalDate.now();
        BigDecimal totalDue = retailerRepository.findAll().stream()
                .filter(r -> !r.isDeleted())
                .map(r -> r.getCurrentDueBalance() == null ? BigDecimal.ZERO : r.getCurrentDueBalance())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new DashboardSummaryDto(
                orderRepository.countByOrderDate(today),
                invoiceRepository.countByInvoiceDate(today),
                deliveryRepository.countByDeliveryDate(today),
                paymentRepository.totalCollection(today),
                retailerRepository.count(),
                productRepository.count(),
                invoiceRepository.countByStatus(InvoiceStatus.PENDING),
                deliveryRepository.countByStatus(DeliveryStatus.PENDING),
                0,
                totalDue
        );
    }
}
