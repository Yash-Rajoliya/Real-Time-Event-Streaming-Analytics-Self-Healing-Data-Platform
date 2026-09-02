package com.platform.aggregation.consumer;

import com.platform.aggregation.model.Event;
import com.platform.aggregation.processor.AggregationProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final AggregationProcessor processor;

    @KafkaListener(topics = "events", groupId = "aggregation-group")
    public void consume(Event event) {
        log.debug("Received event: {}", event.getEventId());
        processor.process(event);
    }
}