package com.platform.selfhealing.detector;

import com.platform.selfhealing.model.LagMetrics;
import org.springframework.stereotype.Component;

@Component
public class LagThresholdDetector {

    private static final long THRESHOLD = 1000;

    public boolean isLagCritical(LagMetrics metrics) {
        return metrics.getLag() > THRESHOLD;
    }
}