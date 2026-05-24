package com.agroerp.controller;

import com.agroerp.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @GetMapping("/daily-orders")
    public ApiResponse<Map<String, Object>> dailyOrders(@RequestParam(required = false) LocalDate date) {
        return placeholder("Daily order report", date);
    }
    @GetMapping("/daily-invoices")
    public ApiResponse<Map<String, Object>> dailyInvoices(@RequestParam(required = false) LocalDate date) {
        return placeholder("Daily invoice report", date);
    }
    @GetMapping("/daily-deliveries")
    public ApiResponse<Map<String, Object>> dailyDeliveries(@RequestParam(required = false) LocalDate date) {
        return placeholder("Daily delivery report", date);
    }
    @GetMapping("/daily-collection")
    public ApiResponse<Map<String, Object>> dailyCollection(@RequestParam(required = false) LocalDate date) {
        return placeholder("Daily collection report", date);
    }
    @GetMapping("/retailer-due")
    public ApiResponse<Map<String, Object>> retailerDue() {
        return ApiResponse.ok("Retailer due report", Map.of("status", "Implement query/export layer"));
    }
    @GetMapping("/stock")
    public ApiResponse<Map<String, Object>> stock() {
        return ApiResponse.ok("Stock report", Map.of("status", "Implement warehouse and batch aggregation"));
    }
    @GetMapping("/profit-loss")
    public ApiResponse<Map<String, Object>> profitLoss() {
        return ApiResponse.ok("Profit/loss report", Map.of("status", "Implement revenue, COGS, discount, return, and expense calculation"));
    }

    private ApiResponse<Map<String, Object>> placeholder(String name, LocalDate date) {
        return ApiResponse.ok(name, Map.of("date", date == null ? LocalDate.now() : date, "status", "Ready for report query implementation"));
    }
}
