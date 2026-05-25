package com.agroerp.service;

import com.agroerp.dto.ReportDtos.ReportResponseDto;

import java.time.LocalDate;

public interface ReportService {
    ReportResponseDto overview(LocalDate fromDate, LocalDate toDate);
    ReportResponseDto orders(LocalDate fromDate, LocalDate toDate);
    ReportResponseDto invoices(LocalDate fromDate, LocalDate toDate);
    ReportResponseDto deliveries(LocalDate fromDate, LocalDate toDate);
    ReportResponseDto payments(LocalDate fromDate, LocalDate toDate);
    ReportResponseDto retailerSales(LocalDate fromDate, LocalDate toDate);
    ReportResponseDto retailerDue();
    ReportResponseDto retailerLedger(Long retailerId, LocalDate fromDate, LocalDate toDate);
    ReportResponseDto productSales(LocalDate fromDate, LocalDate toDate);
    ReportResponseDto stock();
    ReportResponseDto lowStock();
    ReportResponseDto batchStock();
    ReportResponseDto expiryProducts(LocalDate fromDate, LocalDate toDate);
    ReportResponseDto discountOffers();
    ReportResponseDto returns(LocalDate fromDate, LocalDate toDate);
    ReportResponseDto profitLoss(LocalDate fromDate, LocalDate toDate);
}
