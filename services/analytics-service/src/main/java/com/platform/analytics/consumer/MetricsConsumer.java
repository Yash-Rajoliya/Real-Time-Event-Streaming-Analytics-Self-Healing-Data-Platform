package com.platform.analytics.consumer;

import com.platform.analytics.model.Metric;
import com.platform.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MetricsConsumer {

    private final AnalyticsService service;

    @KafkaListener(topics = "aggregated-metrics", groupId = "analytics-group")
    public void consume(Metric metric) {
        try {
            service.process(metric);
        } catch (Exception e) {
            log.error("Failed to process metric", e);
        }
    }
}