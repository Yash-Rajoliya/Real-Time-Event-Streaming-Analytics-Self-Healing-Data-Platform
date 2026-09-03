// services/self-healing-service/src/main/java/com/platform/selfhealing/monitor/KafkaLagMonitor.java
package com.platform.selfhealing.monitor;

import com.platform.selfhealing.model.LagMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaLagMonitor {

    private final MetricsCollector collector;

    @Value("${self-healing.lag.warning-threshold:10000}")
    private long warningThreshold;

    @Value("${self-healing.lag.critical-threshold:50000}")
    private long criticalThreshold;

    /**
     * Retrieves current Kafka lag metrics across consumer groups and evaluates 
     * breach thresholds to guide recovery tuning actions.
     */
    public List<LagMetrics> getLagMetrics() {
        try {
            List<LagMetrics> metrics = collector.collectKafkaLag();
            if (metrics == null || metrics.isEmpty()) {
                log.debug("No active Kafka consumer lag metrics retrieved.");
                return Collections.emptyList();
            }

            return metrics.stream()
                    .map(this::evaluateThresholds)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error collecting Kafka consumer lag metrics: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Inspects metrics for lag violations against critical/warning limits.
     */
    private LagMetrics evaluateThresholds(LagMetrics metrics) {
        long totalLag = metrics.getTotalLag();

        if (totalLag >= criticalThreshold) {
            log.warn("CRITICAL LAG BREACH: Group [{}] on topic [{}] has total lag of {} (Threshold: {})",
                    metrics.getConsumerGroup(), metrics.getTopic(), totalLag, criticalThreshold);
            metrics.setSeverity(LagMetrics.Severity.CRITICAL);
            metrics.setRequiresAutoScaling(true);
        } else if (totalLag >= warningThreshold) {
            log.info("WARNING LAG ELEVATION: Group [{}] on topic [{}] has total lag of {} (Threshold: {})",
                    metrics.getConsumerGroup(), metrics.getTopic(), totalLag, warningThreshold);
            metrics.setSeverity(LagMetrics.Severity.WARNING);
            metrics.setRequiresAutoScaling(false);
        } else {
            metrics.setSeverity(LagMetrics.Severity.NORMAL);
            metrics.setRequiresAutoScaling(false);
        }

        return metrics;
    }
}