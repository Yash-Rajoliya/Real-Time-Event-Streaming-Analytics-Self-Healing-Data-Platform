// services/analytics-service/src/main/java/com/platform/analytics/controller/AnalyticsController.java
package com.platform.analytics.controller;

import com.platform.analytics.model.Metric;
import com.platform.analytics.service.AnalyticsService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/metrics/summary")
    public ResponseEntity<MetricsSummaryResponse> getMetricsSummary(
            @RequestParam(required = false) String metricName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endTime) {
        
        MetricsSummaryResponse response = analyticsService.getSummary(metricName, startTime, endTime);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/metrics/timeseries")
    public ResponseEntity<List<Metric>> getTimeseries(
            @RequestParam String metricName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endTime) {

        List<Metric> metrics = analyticsService.getTimeseriesMetrics(metricName, startTime, endTime);
        return ResponseEntity.ok(metrics);
    }

    // --- DTO Contracts ---

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricsSummaryResponse {
        private String metricName;
        private long totalEvents;
        private double averageValue;
        private double maxValue;
        private double minValue;
        private Map<String, Long> countByCategory;
        private Instant windowStart;
        private Instant windowEnd;
    }
}