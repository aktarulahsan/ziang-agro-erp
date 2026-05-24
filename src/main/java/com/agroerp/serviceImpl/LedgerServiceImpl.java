package com.agroerp.serviceImpl;

import com.agroerp.entity.Retailer;
import com.agroerp.entity.RetailerLedger;
import com.agroerp.enums.LedgerType;
import com.agroerp.exception.ResourceNotFoundException;
import com.agroerp.repository.RetailerLedgerRepository;
import com.agroerp.repository.RetailerRepository;
import com.agroerp.service.LedgerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LedgerServiceImpl implements LedgerService {
    private final RetailerLedgerRepository ledgerRepository;
    private final RetailerRepository retailerRepository;

    public LedgerServiceImpl(RetailerLedgerRepository ledgerRepository, RetailerRepository retailerRepository) {
        this.ledgerRepository = ledgerRepository;
        this.retailerRepository = retailerRepository;
    }

    @Override
    @Transactional
    public RetailerLedger post(Retailer retailer, LocalDate date, LedgerType type, BigDecimal debit,
                               BigDecimal credit, String referenceType, Long referenceId, String narration) {
        BigDecimal newBalance = retailer.getCurrentDueBalance().add(debit).subtract(credit);
        retailer.setCurrentDueBalance(newBalance);
        retailerRepository.save(retailer);

        RetailerLedger ledger = new RetailerLedger();
        ledger.setRetailer(retailer);
        ledger.setTransactionDate(date);
        ledger.setLedgerType(type);
        ledger.setDebitAmount(debit);
        ledger.setCreditAmount(credit);
        ledger.setRunningBalance(newBalance);
        ledger.setReferenceType(referenceType);
        ledger.setReferenceId(referenceId);
        ledger.setNarration(narration);
        return ledgerRepository.save(ledger);
    }

    @Override
    public List<RetailerLedger> retailerLedger(Long retailerId) {
        if (!retailerRepository.existsById(retailerId)) {
            throw new ResourceNotFoundException("Retailer not found");
        }
        return ledgerRepository.findByRetailerIdAndDeletedFalseOrderByTransactionDateAscIdAsc(retailerId);
    }
}
