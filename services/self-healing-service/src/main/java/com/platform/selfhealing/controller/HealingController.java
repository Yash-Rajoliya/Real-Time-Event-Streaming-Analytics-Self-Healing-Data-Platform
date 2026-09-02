package com.platform.selfhealing.controller;

import com.platform.selfhealing.detector.*;
import com.platform.selfhealing.monitor.*;
import com.platform.selfhealing.recovery.*;
import com.platform.selfhealing.scaler.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/self-heal")
@RequiredArgsConstructor
public class HealingController {

    private final KafkaLagMonitor lagMonitor;
    private final LagThresholdDetector lagDetector;
    private final ConsumerRecovery recovery;
    private final ReplayManager replayManager;
    private final AutoScaler scaler;

    @PostMapping("/run")
    public String runHealingCycle() {

        var metricsList = lagMonitor.getLagMetrics();

        metricsList.forEach(metrics -> {

            if (lagDetector.isLagCritical(metrics)) {

                // Step 1: Scale
                scaler.scaleUp(metrics.getConsumerGroup());

                // Step 2: Restart consumer
                recovery.restartConsumer(metrics.getConsumerGroup());

                // Step 3: Replay events
                replayManager.replay(metrics.getTopic(), metrics.getConsumerGroup());
            }
        });

        return "Healing cycle executed";
    }
}