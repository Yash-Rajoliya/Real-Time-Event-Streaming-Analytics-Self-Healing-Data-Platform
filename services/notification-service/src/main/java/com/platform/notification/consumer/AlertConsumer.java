package com.platform.notification.consumer;

import com.platform.notification.model.Alert;
import com.platform.notification.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertConsumer {

    private final AlertService service;

    // Simulated Kafka listener
    public void consume(Alert alert) {
        service.process(alert);
    }
}