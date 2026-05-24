package com.agroerp.service;

import com.agroerp.entity.Retailer;
import com.agroerp.entity.RetailerLedger;
import com.agroerp.enums.LedgerType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LedgerService {
    RetailerLedger post(Retailer retailer, LocalDate date, LedgerType type, BigDecimal debit,
                        BigDecimal credit, String referenceType, Long referenceId, String narration);
    List<RetailerLedger> retailerLedger(Long retailerId);
}
