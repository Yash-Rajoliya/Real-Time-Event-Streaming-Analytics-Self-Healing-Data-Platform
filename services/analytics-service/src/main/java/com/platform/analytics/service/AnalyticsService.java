// services/analytics-service/src/main/java/com/platform/analytics/service/AnalyticsService.java
package com.platform.analytics.service;

import com.platform.analytics.controller.AnalyticsController.MetricsSummaryResponse;
import com.platform.analytics.model.Metric;
import com.platform.analytics.repository.ElasticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final ElasticRepository elasticRepository;
    private final MetricsCalculator metricsCalculator;

    public MetricsSummaryResponse getSummary(String metricName, Instant startTime, Instant endTime) {
        log.debug("Fetching analytics summary for metric [{}] between {} and {}", metricName, startTime, endTime);
        List<Metric> rawMetrics = elasticRepository.findByMetricNameAndTimestampBetween(metricName, startTime, endTime);
        return metricsCalculator.calculateSummary(metricName, rawMetrics, startTime, endTime);
    }

    public List<Metric> getTimeseriesMetrics(String metricName, Instant startTime, Instant endTime) {
        log.debug("Fetching timeseries metrics for [{}] between {} and {}", metricName, startTime, endTime);
        return elasticRepository.findByMetricNameAndTimestampBetween(metricName, startTime, endTime);
    }
}