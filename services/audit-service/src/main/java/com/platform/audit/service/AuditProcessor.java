package com.platform.audit.service;

import com.platform.audit.model.*;
import com.platform.audit.util.AuditUtils;
import org.springframework.stereotype.Component;

@Component
public class AuditProcessor {

    public AuditLog process(AuditEvent event) {

        return AuditLog.builder()
                .id(AuditUtils.generateId())
                .service(event.getService())
                .userId(event.getUserId())
                .action(event.getAction())
                .metadata(event.getMetadata())
                .type(event.getType())
                .timestamp(event.getTimestamp())
                .build();
    }
}