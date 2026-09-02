// services/ingestion-service/src/main/java/com/platform/ingestion/model/Event.java
package com.platform.ingestion.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private String eventId;
    private String eventType;
    private String source;
    private String userId;
    private Instant timestamp;
    private EventMetadata metadata;
    private Map<String, Object> payload;
}