package com.agroerp.controller;

import com.agroerp.dto.WorkflowDtos.DeliveryResponse;
import com.agroerp.entity.Delivery;
import com.agroerp.repository.DeliveryRepository;
import com.agroerp.response.ApiResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
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

    private DeliveryResponse toDto(Delivery delivery) {
        return new DeliveryResponse(delivery.getId(), delivery.getDeliveryNumber(), delivery.getDeliveryDate(),
                delivery.getInvoice().getId(), delivery.getInvoice().getInvoiceNumber(),
                delivery.getRetailer().getId(), delivery.getRetailer().getRetailerName(),
                delivery.getDeliveryPerson(), delivery.getVehicleDetails(), delivery.getStatus());
    }
}
