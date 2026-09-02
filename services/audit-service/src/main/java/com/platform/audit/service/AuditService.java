package com.platform.audit.service;

import com.platform.audit.model.*;
import com.platform.audit.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository repository;
    private final AuditProcessor processor;

    public void handle(AuditEvent event) {

        AuditLog log = processor.process(event);

        repository.save(log);
    }
}