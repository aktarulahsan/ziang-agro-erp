package com.agroerp.repository;

import com.agroerp.entity.AccountingEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountingEntryRepository extends JpaRepository<AccountingEntry, Long> {
    List<AccountingEntry> findByProductProductCodeOrderByPostingDateDescIdDesc(String productCode);
    List<AccountingEntry> findByMaterialDocumentNumberOrderByIdAsc(String materialDocumentNumber);
}
