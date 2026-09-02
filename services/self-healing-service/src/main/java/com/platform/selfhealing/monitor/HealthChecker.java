package com.platform.selfhealing.monitor;

import com.platform.selfhealing.model.ServiceHealth;
import org.springframework.stereotype.Component;

@Component
public class HealthChecker {

    public ServiceHealth check(String service) {
        // Simulated health
        return new ServiceHealth(service, true, "OK");
    }
}