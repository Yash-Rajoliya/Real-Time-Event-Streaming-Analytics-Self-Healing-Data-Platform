package com.platform.aggregation.producer;

import com.platform.aggregation.model.AggregatedMetric;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AggregationProducer {

    private final KafkaTemplate<String, AggregatedMetric> kafkaTemplate;

    public void publish(AggregatedMetric metric) {
        kafkaTemplate.send("aggregated-metrics", metric);
    }
}