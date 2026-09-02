package com.platform.audit.repository;

import com.platform.audit.model.AuditLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class AuditRepository {

    public void save(AuditLog log) {
        // Replace with DB / Elasticsearch
        log.info("Persisting audit log: {}", log);
    }
}