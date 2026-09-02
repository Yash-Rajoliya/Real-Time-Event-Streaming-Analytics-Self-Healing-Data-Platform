package com.platform.selfhealing.monitor;

import com.platform.selfhealing.model.LagMetrics;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MetricsCollector {

    public List<LagMetrics> collectKafkaLag() {
        // Simulated metrics (replace with Kafka Admin API)
        return List.of(
                new LagMetrics("events", "group-1", new Random().nextInt(5000))
        );
    }
}