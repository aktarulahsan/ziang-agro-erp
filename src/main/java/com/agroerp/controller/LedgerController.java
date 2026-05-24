package com.agroerp.controller;

import com.agroerp.dto.LedgerDtos.LedgerEntryResponse;
import com.agroerp.response.ApiResponse;
import com.agroerp.service.LedgerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ledger")
public class LedgerController {
    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @GetMapping("/retailers/{retailerId}")
    public ApiResponse<List<LedgerEntryResponse>> retailerLedger(@PathVariable Long retailerId) {
        return ApiResponse.ok("Retailer ledger loaded", ledgerService.retailerLedger(retailerId).stream()
                .map(l -> new LedgerEntryResponse(l.getId(), l.getTransactionDate(), l.getLedgerType(),
                        l.getDebitAmount(), l.getCreditAmount(), l.getRunningBalance(),
                        l.getReferenceType(), l.getReferenceId(), l.getNarration()))
                .toList());
    }
}
