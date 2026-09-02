package com.platform.selfhealing.detector;

import com.platform.selfhealing.model.ServiceHealth;
import org.springframework.stereotype.Component;

@Component
public class FailureDetector {

    public boolean isFailed(ServiceHealth health) {
        return !health.isHealthy();
    }
}