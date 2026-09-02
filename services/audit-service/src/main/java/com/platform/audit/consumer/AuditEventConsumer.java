package com.platform.audit.consumer;

import com.platform.audit.model.AuditEvent;
import com.platform.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditEventConsumer {

    private final AuditService service;

    // Simulated Kafka listener
    public void consume(AuditEvent event) {
        service.handle(event);
    }
}