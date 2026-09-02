// services/query-service/src/main/java/com/platform/query/model/MetricDTO.java
package com.platform.query.model;

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
public class MetricDTO {

    private String id;
    private String metricName;
    private String category;
    private double value;
    private Instant timestamp;
    private Map<String, String> tags;
}