package com.platform.aggregation.scheduler;

import com.platform.aggregation.processor.AggregationProcessor;
import com.platform.aggregation.producer.AggregationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MetricsScheduler {

    private final AggregationProcessor processor;
    private final AggregationProducer producer;

    @Scheduled(fixedRate = 5000)
    public void publishMetrics() {
        producer.publish(processor.snapshot());
    }
}