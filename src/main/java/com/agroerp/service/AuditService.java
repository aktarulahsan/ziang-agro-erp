package com.agroerp.service;

public interface AuditService {
    void log(String module, String action, Long recordId, String details);
}
