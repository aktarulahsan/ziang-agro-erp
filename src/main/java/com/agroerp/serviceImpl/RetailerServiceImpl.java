package com.agroerp.serviceImpl;

import com.agroerp.dto.RetailerDto;
import com.agroerp.entity.Retailer;
import com.agroerp.entity.Territory;
import com.agroerp.enums.LedgerType;
import com.agroerp.exception.BusinessException;
import com.agroerp.exception.ResourceNotFoundException;
import com.agroerp.mapper.RetailerMapper;
import com.agroerp.repository.RetailerRepository;
import com.agroerp.repository.TerritoryRepository;
import com.agroerp.service.AuditService;
import com.agroerp.service.LedgerService;
import com.agroerp.service.RetailerService;
import com.agroerp.util.NumberGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class RetailerServiceImpl implements RetailerService {
    private final RetailerRepository retailerRepository;
    private final TerritoryRepository territoryRepository;
    private final LedgerService ledgerService;
    private final AuditService auditService;
    private final NumberGenerator numberGenerator;

    public RetailerServiceImpl(RetailerRepository retailerRepository, TerritoryRepository territoryRepository,
                               LedgerService ledgerService, AuditService auditService, NumberGenerator numberGenerator) {
        this.retailerRepository = retailerRepository;
        this.territoryRepository = territoryRepository;
        this.ledgerService = ledgerService;
        this.auditService = auditService;
        this.numberGenerator = numberGenerator;
    }

    @Override
    @Transactional
    public RetailerDto create(RetailerDto dto) {
        String retailerCode = clean(dto.retailerCode());
        if (retailerCode == null) {
            retailerCode = nextRetailerCode();
        }
        if (retailerRepository.existsByRetailerCode(retailerCode)) {
            throw new BusinessException("Retailer code already exists");
        }
        Retailer retailer = new Retailer();
        apply(dto, retailer);
        retailer.setRetailerCode(retailerCode);
        retailer.setCurrentDueBalance(dto.openingBalance());
        Retailer saved = retailerRepository.save(retailer);
        if (dto.openingBalance().compareTo(BigDecimal.ZERO) > 0) {
            ledgerService.post(saved, java.time.LocalDate.now(), LedgerType.OPENING, dto.openingBalance(),
                    BigDecimal.ZERO, "RETAILER", saved.getId(), "Opening balance");
        }
        auditService.log("Retailer", "CREATE", saved.getId(), saved.getRetailerCode());
        return RetailerMapper.toDto(saved);
    }

    @Override
    @Transactional
    public RetailerDto update(Long id, RetailerDto dto) {
        Retailer retailer = retailerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Retailer not found"));
        apply(dto, retailer);
        auditService.log("Retailer", "UPDATE", id, dto.retailerCode());
        return RetailerMapper.toDto(retailerRepository.save(retailer));
    }

    @Override
    public RetailerDto get(Long id) {
        return RetailerMapper.toDto(retailerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Retailer not found")));
    }

    @Override
    public Page<RetailerDto> search(String keyword, Pageable pageable) {
        String q = keyword == null ? "" : keyword;
        return retailerRepository
                .findByDeletedFalseAndRetailerNameContainingIgnoreCaseOrDeletedFalseAndMobileNumberContainingIgnoreCase(q, q, pageable)
                .map(RetailerMapper::toDto);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        Retailer retailer = retailerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Retailer not found"));
        retailer.setDeleted(true);
        retailer.setActive(false);
        auditService.log("Retailer", "DELETE", id, retailer.getRetailerCode());
    }

    private void apply(RetailerDto dto, Retailer retailer) {
        if (clean(dto.retailerCode()) != null) {
            retailer.setRetailerCode(clean(dto.retailerCode()));
        }
        retailer.setRetailerName(dto.retailerName());
        retailer.setOwnerName(dto.ownerName());
        retailer.setMobileNumber(dto.mobileNumber());
        retailer.setEmail(dto.email());
        retailer.setAddress(dto.address());
        retailer.setMarketName(dto.marketName());
        retailer.setCreditLimit(dto.creditLimit());
        retailer.setOpeningBalance(dto.openingBalance());
        retailer.setActive(dto.active());
        if (dto.territoryId() != null) {
            Territory territory = territoryRepository.findById(dto.territoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Territory not found"));
            retailer.setTerritory(territory);
        }
    }

    public String nextRetailerCode() {
        return numberGenerator.nextFromDatabase("RET",
                retailerRepository::findMaxRetailerCodeForPrefix,
                retailerRepository::existsByRetailerCode);
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
