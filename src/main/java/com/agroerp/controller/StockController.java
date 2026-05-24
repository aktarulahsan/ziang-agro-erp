package com.agroerp.controller;

import com.agroerp.dto.StockDtos.StockBalanceResponse;
import com.agroerp.dto.StockDtos.StockTransactionRequest;
import com.agroerp.response.ApiResponse;
import com.agroerp.service.StockService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/transactions")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','STORE_USER')")
    public ApiResponse<Void> transaction(@Valid @RequestBody StockTransactionRequest request) {
        stockService.postTransaction(request, "MANUAL", null);
        return ApiResponse.ok("Stock transaction posted", null);
    }

    @GetMapping("/balance")
    public ApiResponse<StockBalanceResponse> balance(@RequestParam Long productId, @RequestParam Long warehouseId) {
        return ApiResponse.ok("Stock balance loaded", stockService.balance(productId, warehouseId));
    }
}
