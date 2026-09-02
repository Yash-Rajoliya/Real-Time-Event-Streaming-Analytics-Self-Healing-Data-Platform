package com.platform.selfhealing.monitor;

import com.platform.selfhealing.model.LagMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KafkaLagMonitor {

    private final MetricsCollector collector;

    public List<LagMetrics> getLagMetrics() {
        return collector.collectKafkaLag();
    }
}