// services/ingestion-service/src/main/java/com/platform/ingestion/service/EventService.java
package com.platform.ingestion.service;

import com.platform.ingestion.controller.EventController.EventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.ingested-events:raw-events-topic}")
    private String rawEventsTopic;

    public Mono<String> processAndPublish(EventPayload payload, String correlationId) {
        String eventId = "evt_" + UUID.randomUUID().toString().replace("-", "");
        
        Map<String, Object> eventRecord = Map.of(
                "eventId", eventId,
                "eventType", payload.getEventType(),
                "source", payload.getSource(),
                "userId", payload.getUserId() != null ? payload.getUserId() : "anonymous",
                "timestamp", payload.getTimestamp().toEpochMilli(),
                "correlationId", correlationId != null ? correlationId : "",
                "payload", payload.getData()
        );

        String partitioningKey = payload.getUserId() != null ? payload.getUserId() : eventId;

        return Mono.fromFuture(() -> kafkaTemplate.send(rawEventsTopic, partitioningKey, eventRecord))
                .doOnSuccess(result -> log.debug("Published event [{}] to partition [{}] with offset [{}]",
                        eventId, result.getRecordMetadata().partition(), result.getRecordMetadata().offset()))
                .doOnError(ex -> log.error("Failed to stream event [{}] to Kafka: {}", eventId, ex.getMessage(), ex))
                .thenReturn(eventId);
    }
}