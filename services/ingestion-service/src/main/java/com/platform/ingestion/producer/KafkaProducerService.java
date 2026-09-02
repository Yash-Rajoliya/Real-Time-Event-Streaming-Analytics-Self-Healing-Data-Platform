// services/ingestion-service/src/main/java/com/platform/ingestion/producer/KafkaProducerService.java
package com.platform.ingestion.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.ingested-events:raw-events-topic}")
    private String primaryTopic;

    public Mono<SendResult<String, Object>> sendEvent(String key, Map<String, Object> payload, String correlationId) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(primaryTopic, key, payload);
        
        if (correlationId != null && !correlationId.isBlank()) {
            record.headers().add(new RecordHeader("X-Correlation-ID", correlationId.getBytes(StandardCharsets.UTF_8)));
        }

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(record);

        return Mono.fromFuture(future)
                .doOnSuccess(result -> log.debug("Published event key [{}] to partition [{}] with offset [{}]",
                        key, result.getRecordMetadata().partition(), result.getRecordMetadata().offset()))
                .doOnError(ex -> log.error("Error publishing event key [{}] to topic [{}]: {}",
                        key, primaryTopic, ex.getMessage()));
    }
}