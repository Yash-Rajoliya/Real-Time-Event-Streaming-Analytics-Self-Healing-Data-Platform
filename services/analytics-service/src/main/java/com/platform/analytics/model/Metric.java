// services/analytics-service/src/main/java/com/platform/analytics/model/Metric.java
package com.platform.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "platform-metrics")
public class Metric {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String metricName;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Double)
    private double value;

    @Field(type = FieldType.Date)
    private Instant timestamp;

    @Field(type = FieldType.Object)
    private Map<String, String> tags;
}