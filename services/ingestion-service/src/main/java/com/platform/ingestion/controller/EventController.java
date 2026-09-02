// services/ingestion-service/src/main/java/com/platform/ingestion/controller/EventController.java
package com.platform.ingestion.controller;

import com.platform.ingestion.service.BatchService;
import com.platform.ingestion.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final BatchService batchService;

    @PostMapping("/ingest")
    public Mono<ResponseEntity<IngestResponse>> ingestSingleEvent(
            @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId,
            @Valid @RequestBody EventPayload payload) {

        return eventService.processAndPublish(payload, correlationId)
                .map(eventId -> ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(IngestResponse.builder()
                                .status("ACCEPTED")
                                .eventId(eventId)
                                .timestamp(Instant.now())
                                .build()));
    }

    @PostMapping("/ingest/batch")
    public Mono<ResponseEntity<BatchIngestResponse>> ingestBatchEvents(
            @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId,
            @Valid @RequestBody BatchEventPayload payload) {

        return batchService.processBatch(payload.getEvents(), correlationId)
                .map(result -> ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(BatchIngestResponse.builder()
                                .status("ACCEPTED")
                                .acceptedCount(result.getAcceptedCount())
                                .rejectedCount(result.getRejectedCount())
                                .timestamp(Instant.now())
                                .build()));
    }

    // --- DTO Models ---

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventPayload {
        @NotBlank(message = "Event type is required")
        private String eventType;

        @NotBlank(message = "Source system is required")
        private String source;

        private String userId;

        @NotNull(message = "Timestamp is required")
        private Instant timestamp;

        @NotEmpty(message = "Event data payload cannot be empty")
        private Map<String, Object> data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BatchEventPayload {
        @NotEmpty(message = "Batch list cannot be empty")
        private List<@Valid EventPayload> events;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IngestResponse {
        private String status;
        private String eventId;
        private Instant timestamp;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BatchIngestResponse {
        private String status;
        private int acceptedCount;
        private int rejectedCount;
        private Instant timestamp;
    }
}