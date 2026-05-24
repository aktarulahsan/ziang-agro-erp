package com.agroerp.serviceImpl;

import com.agroerp.dto.OrderDtos.OrderItemRequest;
import com.agroerp.dto.OrderDtos.OrderRequest;
import com.agroerp.dto.OrderDtos.OrderResponse;
import com.agroerp.entity.Order;
import com.agroerp.entity.OrderItem;
import com.agroerp.entity.Product;
import com.agroerp.entity.Retailer;
import com.agroerp.enums.OrderStatus;
import com.agroerp.exception.BusinessException;
import com.agroerp.exception.ResourceNotFoundException;
import com.agroerp.mapper.OrderMapper;
import com.agroerp.repository.OrderRepository;
import com.agroerp.repository.ProductRepository;
import com.agroerp.repository.RetailerRepository;
import com.agroerp.repository.StockTransactionRepository;
import com.agroerp.repository.WarehouseRepository;
import com.agroerp.service.AuditService;
import com.agroerp.service.OrderService;
import com.agroerp.util.NumberGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final RetailerRepository retailerRepository;
    private final ProductRepository productRepository;
    private final StockTransactionRepository stockRepository;
    private final WarehouseRepository warehouseRepository;
    private final NumberGenerator numberGenerator;
    private final AuditService auditService;

    public OrderServiceImpl(OrderRepository orderRepository, RetailerRepository retailerRepository,
                            ProductRepository productRepository, StockTransactionRepository stockRepository,
                            WarehouseRepository warehouseRepository, NumberGenerator numberGenerator, AuditService auditService) {
        this.orderRepository = orderRepository;
        this.retailerRepository = retailerRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.warehouseRepository = warehouseRepository;
        this.numberGenerator = numberGenerator;
        this.auditService = auditService;
    }

    @Override
    @Transactional
    public OrderResponse create(OrderRequest request) {
        Retailer retailer = retailerRepository.findById(request.retailerId())
                .orElseThrow(() -> new ResourceNotFoundException("Retailer not found"));
        Order order = new Order();
        order.setOrderNumber(numberGenerator.nextFromDatabase("ORD",
                orderRepository::findMaxOrderNumberForPrefix,
                orderRepository::existsByOrderNumber));
        order.setOrderDate(LocalDate.now());
        order.setRetailer(retailer);
        order.setStatus(OrderStatus.PENDING);
        Long defaultWarehouseId = warehouseRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"))
                .getId();
        Map<Long, BigDecimal> requestedByProduct = new HashMap<>();
        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            BigDecimal requested = requestedByProduct.merge(product.getId(), itemRequest.quantity(), BigDecimal::add);
            BigDecimal available = stockRepository.currentStock(product.getId(), defaultWarehouseId);
            if (requested.compareTo(available) > 0) {
                throw new BusinessException(product.getProductCode() + " stock is " + available + ", requested " + requested);
            }
            OrderItem item = calculateItem(product, itemRequest.quantity());
            item.setOrder(order);
            order.getItems().add(item);
            order.setGrossAmount(order.getGrossAmount().add(item.getUnitPrice().multiply(item.getQuantity())));
            order.setDiscountAmount(order.getDiscountAmount().add(item.getDiscountAmount()));
            order.setVatAmount(order.getVatAmount().add(item.getVatAmount()));
            order.setNetAmount(order.getNetAmount().add(item.getLineTotal()));
        }
        Order saved = orderRepository.save(order);
        auditService.log("Order", "CREATE", saved.getId(), saved.getOrderNumber());
        return OrderMapper.toDto(saved);
    }

    @Override
    @Transactional
    public OrderResponse approve(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.DRAFT) {
            throw new BusinessException("Only draft or pending orders can be approved");
        }
        BigDecimal projectedDue = order.getRetailer().getCurrentDueBalance().add(order.getNetAmount());
        if (projectedDue.compareTo(order.getRetailer().getCreditLimit()) > 0) {
            throw new BusinessException("Credit limit exceeded. Limit: " + order.getRetailer().getCreditLimit() + ", projected due: " + projectedDue);
        }
        order.setStatus(OrderStatus.APPROVED);
        auditService.log("Order", "APPROVE", orderId, order.getOrderNumber());
        return OrderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse get(Long orderId) {
        return OrderMapper.toDto(orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> list(Pageable pageable) {
        return orderRepository.findAllByOrderByIdDesc(pageable).map(OrderMapper::toDto);
    }

    private OrderItem calculateItem(Product product, BigDecimal quantity) {
        BigDecimal price = product.getRetailerPrice().compareTo(BigDecimal.ZERO) > 0 ? product.getRetailerPrice() : product.getSalesPrice();
        BigDecimal gross = price.multiply(quantity);
        BigDecimal discount = gross.multiply(product.getDiscountPercent()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal taxable = gross.subtract(discount);
        BigDecimal vat = taxable.multiply(product.getVatPercent()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPrice(price);
        item.setDiscountAmount(discount);
        item.setVatAmount(vat);
        item.setLineTotal(taxable.add(vat));
        return item;
    }
}
