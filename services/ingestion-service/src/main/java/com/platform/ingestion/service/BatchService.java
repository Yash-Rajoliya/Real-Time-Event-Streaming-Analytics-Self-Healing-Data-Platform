// services/ingestion-service/src/main/java/com/platform/ingestion/service/BatchService.java
package com.platform.ingestion.service;

import com.platform.ingestion.controller.EventController.EventPayload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchService {

    private final EventService eventService;

    public Mono<BatchProcessingResult> processBatch(List<EventPayload> events, String correlationId) {
        return Flux.fromIterable(events)
                .flatMap(payload -> eventService.processAndPublish(payload, correlationId)
                        .map(eventId -> true)
                        .onErrorResume(ex -> {
                            log.error("Batch processing error for item source [{}]: {}", payload.getSource(), ex.getMessage());
                            return Mono.just(false);
                        }))
                .reduce(new BatchAccumulator(), (acc, success) -> {
                    if (success) {
                        acc.accepted++;
                    } else {
                        acc.rejected++;
                    }
                    return acc;
                })
                .map(acc -> new BatchProcessingResult(acc.accepted, acc.rejected));
    }

    private static class BatchAccumulator {
        int accepted = 0;
        int rejected = 0;
    }

    @Data
    @AllArgsConstructor
    public static class BatchProcessingResult {
        private int acceptedCount;
        private int rejectedCount;
    }
}