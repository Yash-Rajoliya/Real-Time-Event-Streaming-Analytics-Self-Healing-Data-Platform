// services/ingestion-service/src/main/java/com/platform/ingestion/producer/DeadLetterProducer.java
package com.platform.ingestion.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeadLetterProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.dead-letter:raw-events-dlq-topic}")
    private String dlqTopic;

    public Mono<Void> sendToDlq(String key, Object rawPayload, String reason, Throwable cause, String correlationId) {
        Map<String, Object> dlqEnvelope = new HashMap<>();
        dlqEnvelope.put("failedPayload", rawPayload);
        dlqEnvelope.put("failureReason", reason);
        dlqEnvelope.put("exceptionMessage", cause != null ? cause.getMessage() : "N/A");
        dlqEnvelope.put("failedAt", Instant.now().toString());

        ProducerRecord<String, Object> record = new ProducerRecord<>(dlqTopic, key, dlqEnvelope);
        if (correlationId != null && !correlationId.isBlank()) {
            record.headers().add(new RecordHeader("X-Correlation-ID", correlationId.getBytes(StandardCharsets.UTF_8)));
        }

        return Mono.fromFuture(() -> kafkaTemplate.send(record))
                .doOnSuccess(r -> log.warn("Enqueued failed event [{}] to DLQ topic [{}]", key, dlqTopic))
                .doOnError(ex -> log.error("CRITICAL: Failed to enqueue event [{}] to DLQ: {}", key, ex.getMessage(), ex))
                .then();
    }
}