// services/ingestion-service/src/main/java/com/platform/ingestion/model/EventMetadata.java
package com.platform.ingestion.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventMetadata {

    private String correlationId;
    private String ipAddress;
    private String userAgent;
    private String schemaVersion;
    private Instant ingestedAt;
}