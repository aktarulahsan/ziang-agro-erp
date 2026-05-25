package com.agroerp.controller;

import com.agroerp.dto.ReportDtos.ReportResponseDto;
import com.agroerp.response.ApiResponse;
import com.agroerp.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/overview")
    public ApiResponse<ReportResponseDto> overview(@RequestParam(required = false) String period,
                                                   @RequestParam(required = false) LocalDate date,
                                                   @RequestParam(required = false) String month,
                                                   @RequestParam(required = false) LocalDate fromDate,
                                                   @RequestParam(required = false) LocalDate toDate) {
        DateWindow window = resolveWindow(period, date, month, fromDate, toDate);
        return ok(reportService.overview(window.fromDate(), window.toDate()));
    }

    @GetMapping("/orders")
    public ApiResponse<ReportResponseDto> orders(@RequestParam(required = false) String period,
                                                 @RequestParam(required = false) LocalDate date,
                                                 @RequestParam(required = false) String month,
                                                 @RequestParam(required = false) LocalDate fromDate,
                                                 @RequestParam(required = false) LocalDate toDate) {
        DateWindow window = resolveWindow(period, date, month, fromDate, toDate);
        return ok(reportService.orders(window.fromDate(), window.toDate()));
    }

    @GetMapping("/invoices")
    public ApiResponse<ReportResponseDto> invoices(@RequestParam(required = false) String period,
                                                   @RequestParam(required = false) LocalDate date,
                                                   @RequestParam(required = false) String month,
                                                   @RequestParam(required = false) LocalDate fromDate,
                                                   @RequestParam(required = false) LocalDate toDate) {
        DateWindow window = resolveWindow(period, date, month, fromDate, toDate);
        return ok(reportService.invoices(window.fromDate(), window.toDate()));
    }

    @GetMapping("/deliveries")
    public ApiResponse<ReportResponseDto> deliveries(@RequestParam(required = false) String period,
                                                     @RequestParam(required = false) LocalDate date,
                                                     @RequestParam(required = false) String month,
                                                     @RequestParam(required = false) LocalDate fromDate,
                                                     @RequestParam(required = false) LocalDate toDate) {
        DateWindow window = resolveWindow(period, date, month, fromDate, toDate);
        return ok(reportService.deliveries(window.fromDate(), window.toDate()));
    }

    @GetMapping({"/payments", "/daily-collection"})
    public ApiResponse<ReportResponseDto> payments(@RequestParam(required = false) String period,
                                                   @RequestParam(required = false) LocalDate date,
                                                   @RequestParam(required = false) String month,
                                                   @RequestParam(required = false) LocalDate fromDate,
                                                   @RequestParam(required = false) LocalDate toDate) {
        DateWindow window = resolveWindow(period, date, month, fromDate, toDate);
        return ok(reportService.payments(window.fromDate(), window.toDate()));
    }

    @GetMapping("/retailer-sales")
    public ApiResponse<ReportResponseDto> retailerSales(@RequestParam(required = false) String period,
                                                        @RequestParam(required = false) LocalDate date,
                                                        @RequestParam(required = false) String month,
                                                        @RequestParam(required = false) LocalDate fromDate,
                                                        @RequestParam(required = false) LocalDate toDate) {
        DateWindow window = resolveWindow(period, date, month, fromDate, toDate);
        return ok(reportService.retailerSales(window.fromDate(), window.toDate()));
    }

    @GetMapping("/retailer-due")
    public ApiResponse<ReportResponseDto> retailerDue() {
        return ok(reportService.retailerDue());
    }

    @GetMapping("/retailer-ledger")
    public ApiResponse<ReportResponseDto> retailerLedger(@RequestParam(required = false) Long retailerId,
                                                         @RequestParam(required = false) String period,
                                                         @RequestParam(required = false) LocalDate date,
                                                         @RequestParam(required = false) String month,
                                                         @RequestParam(required = false) LocalDate fromDate,
                                                         @RequestParam(required = false) LocalDate toDate) {
        DateWindow window = resolveWindow(period, date, month, fromDate, toDate);
        return ok(reportService.retailerLedger(retailerId, window.fromDate(), window.toDate()));
    }

    @GetMapping("/product-sales")
    public ApiResponse<ReportResponseDto> productSales(@RequestParam(required = false) String period,
                                                       @RequestParam(required = false) LocalDate date,
                                                       @RequestParam(required = false) String month,
                                                       @RequestParam(required = false) LocalDate fromDate,
                                                       @RequestParam(required = false) LocalDate toDate) {
        DateWindow window = resolveWindow(period, date, month, fromDate, toDate);
        return ok(reportService.productSales(window.fromDate(), window.toDate()));
    }

    @GetMapping("/stock")
    public ApiResponse<ReportResponseDto> stock() {
        return ok(reportService.stock());
    }

    @GetMapping("/low-stock")
    public ApiResponse<ReportResponseDto> lowStock() {
        return ok(reportService.lowStock());
    }

    @GetMapping("/batch-stock")
    public ApiResponse<ReportResponseDto> batchStock() {
        return ok(reportService.batchStock());
    }

    @GetMapping("/expiry-products")
    public ApiResponse<ReportResponseDto> expiryProducts(@RequestParam(required = false) String period,
                                                         @RequestParam(required = false) LocalDate date,
                                                         @RequestParam(required = false) String month,
                                                         @RequestParam(required = false) LocalDate fromDate,
                                                         @RequestParam(required = false) LocalDate toDate) {
        DateWindow window = resolveWindow(period, date, month, fromDate, toDate);
        return ok(reportService.expiryProducts(window.fromDate(), window.toDate()));
    }

    @GetMapping("/discount-offers")
    public ApiResponse<ReportResponseDto> discountOffers() {
        return ok(reportService.discountOffers());
    }

    @GetMapping("/returns")
    public ApiResponse<ReportResponseDto> returns(@RequestParam(required = false) String period,
                                                  @RequestParam(required = false) LocalDate date,
                                                  @RequestParam(required = false) String month,
                                                  @RequestParam(required = false) LocalDate fromDate,
                                                  @RequestParam(required = false) LocalDate toDate) {
        DateWindow window = resolveWindow(period, date, month, fromDate, toDate);
        return ok(reportService.returns(window.fromDate(), window.toDate()));
    }

    @GetMapping("/profit-loss")
    public ApiResponse<ReportResponseDto> profitLoss(@RequestParam(required = false) String period,
                                                     @RequestParam(required = false) LocalDate date,
                                                     @RequestParam(required = false) String month,
                                                     @RequestParam(required = false) LocalDate fromDate,
                                                     @RequestParam(required = false) LocalDate toDate) {
        DateWindow window = resolveWindow(period, date, month, fromDate, toDate);
        return ok(reportService.profitLoss(window.fromDate(), window.toDate()));
    }

    @GetMapping("/daily-orders")
    public ApiResponse<ReportResponseDto> dailyOrders(@RequestParam(required = false) LocalDate date) {
        LocalDate reportDate = date == null ? LocalDate.now() : date;
        return ok(reportService.orders(reportDate, reportDate));
    }

    @GetMapping("/daily-invoices")
    public ApiResponse<ReportResponseDto> dailyInvoices(@RequestParam(required = false) LocalDate date) {
        LocalDate reportDate = date == null ? LocalDate.now() : date;
        return ok(reportService.invoices(reportDate, reportDate));
    }

    @GetMapping("/daily-deliveries")
    public ApiResponse<ReportResponseDto> dailyDeliveries(@RequestParam(required = false) LocalDate date) {
        LocalDate reportDate = date == null ? LocalDate.now() : date;
        return ok(reportService.deliveries(reportDate, reportDate));
    }

    private ApiResponse<ReportResponseDto> ok(ReportResponseDto report) {
        return ApiResponse.ok(report.title(), report);
    }

    private DateWindow resolveWindow(String period, LocalDate date, String month, LocalDate fromDate, LocalDate toDate) {
        String requestedPeriod = period == null ? "daily" : period.toLowerCase();
        if ("monthly".equals(requestedPeriod)) {
            YearMonth yearMonth = month == null || month.isBlank() ? YearMonth.now() : YearMonth.parse(month);
            return new DateWindow(yearMonth.atDay(1), yearMonth.atEndOfMonth());
        }
        if ("range".equals(requestedPeriod)) {
            LocalDate from = fromDate == null ? LocalDate.now() : fromDate;
            LocalDate to = toDate == null ? from : toDate;
            return normalize(from, to);
        }
        LocalDate reportDate = date == null ? LocalDate.now() : date;
        return new DateWindow(reportDate, reportDate);
    }

    private DateWindow normalize(LocalDate fromDate, LocalDate toDate) {
        return fromDate.isAfter(toDate) ? new DateWindow(toDate, fromDate) : new DateWindow(fromDate, toDate);
    }

    private record DateWindow(LocalDate fromDate, LocalDate toDate) {
    }
}
