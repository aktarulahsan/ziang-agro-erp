package com.agroerp.controller;

import com.agroerp.dto.WorkflowDtos.DeliveryPrintResponse;
import com.agroerp.dto.WorkflowDtos.DeliveryResponse;
import com.agroerp.dto.WorkflowDtos.PrintItemResponse;
import com.agroerp.entity.Delivery;
import com.agroerp.entity.DeliveryItem;
import com.agroerp.repository.DeliveryRepository;
import com.agroerp.response.ApiResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {
    private final DeliveryRepository deliveryRepository;

    public DeliveryController(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ApiResponse<List<DeliveryResponse>> list() {
        return ApiResponse.ok("Deliveries loaded", deliveryRepository.findAllByOrderByIdDesc().stream().map(this::toDto).toList());
    }

    @GetMapping("/{id}/print")
    @Transactional(readOnly = true)
    public ApiResponse<DeliveryPrintResponse> print(@PathVariable Long id) {
        return ApiResponse.ok("Delivery challan print loaded", deliveryRepository.findPrintById(id).map(this::toPrintDto).orElseThrow());
    }

    private DeliveryResponse toDto(Delivery delivery) {
        return new DeliveryResponse(delivery.getId(), delivery.getDeliveryNumber(), delivery.getDeliveryDate(),
                delivery.getInvoice().getId(), delivery.getInvoice().getInvoiceNumber(),
                delivery.getRetailer().getId(), delivery.getRetailer().getRetailerName(),
                delivery.getDeliveryPerson(), delivery.getVehicleDetails(), delivery.getStatus());
    }

    private DeliveryPrintResponse toPrintDto(Delivery delivery) {
        var retailer = delivery.getRetailer();
        var invoice = delivery.getInvoice();
        return new DeliveryPrintResponse(delivery.getId(), delivery.getDeliveryNumber(), delivery.getDeliveryDate(), delivery.getStatus(),
                invoice.getId(), invoice.getInvoiceNumber(), invoice.getInvoiceDate(),
                retailer.getId(), retailer.getRetailerCode(), retailer.getRetailerName(), retailer.getOwnerName(),
                retailer.getMobileNumber(), retailer.getAddress(), delivery.getDeliveryAddress(),
                delivery.getVehicleDetails(), delivery.getDeliveryPerson(), delivery.getDeliveryContactNo(),
                delivery.getRouteName(), delivery.getTransportCost(), delivery.getRemarks(),
                delivery.getItems().stream().map(this::toPrintItem).toList());
    }

    private PrintItemResponse toPrintItem(DeliveryItem item) {
        var product = item.getProduct();
        return new PrintItemResponse(product.getId(), product.getProductCode(), product.getProductName(),
                item.getDeliveredQuantity(), java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO,
                java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO);
    }
}
