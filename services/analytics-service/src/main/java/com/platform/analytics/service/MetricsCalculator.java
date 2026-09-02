// services/analytics-service/src/main/java/com/platform/analytics/service/MetricsCalculator.java
package com.platform.analytics.service;

import com.platform.analytics.controller.AnalyticsController.MetricsSummaryResponse;
import com.platform.analytics.model.Metric;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MetricsCalculator {

    public MetricsSummaryResponse calculateSummary(String metricName, List<Metric> metrics, Instant windowStart, Instant windowEnd) {
        if (metrics == null || metrics.isEmpty()) {
            return MetricsSummaryResponse.builder()
                    .metricName(metricName != null ? metricName : "ALL")
                    .totalEvents(0)
                    .averageValue(0.0)
                    .maxValue(0.0)
                    .minValue(0.0)
                    .countByCategory(Map.of())
                    .windowStart(windowStart)
                    .windowEnd(windowEnd)
                    .build();
        }

        DoubleSummaryStatistics stats = metrics.stream()
                .mapToDouble(Metric::getValue)
                .summaryStatistics();

        Map<String, Long> categoryCounts = metrics.stream()
                .filter(m -> m.getCategory() != null)
                .collect(Collectors.groupingBy(Metric::getCategory, Collectors.counting()));

        return MetricsSummaryResponse.builder()
                .metricName(metricName != null ? metricName : "ALL")
                .totalEvents(stats.getCount())
                .averageValue(stats.getAverage())
                .maxValue(stats.getMax())
                .minValue(stats.getMin())
                .countByCategory(categoryCounts)
                .windowStart(windowStart)
                .windowEnd(windowEnd)
                .build();
    }
}