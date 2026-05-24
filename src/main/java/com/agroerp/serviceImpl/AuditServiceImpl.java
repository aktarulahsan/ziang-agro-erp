package com.agroerp.serviceImpl;

import com.agroerp.entity.AuditLog;
import com.agroerp.repository.AuditLogRepository;
import com.agroerp.service.AuditService;
import org.springframework.stereotype.Service;

@Service
public class AuditServiceImpl implements AuditService {
    private final AuditLogRepository repository;

    public AuditServiceImpl(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void log(String module, String action, Long recordId, String details) {
        AuditLog log = new AuditLog();
        log.setModuleName(module);
        log.setActionName(action);
        log.setRecordId(recordId);
        log.setDetails(details);
        repository.save(log);
    }
}
