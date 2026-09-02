// services/data-governance-service/src/main/java/com/platform/governance/config/GovernanceConfig.java
package com.platform.governance.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "governance")
public class GovernanceConfig {

    private boolean piiMaskingEnabled = true;
    private int defaultRetentionDays = 365;
    private Map<String, Integer> retentionDaysByDataset = new HashMap<>();
    private RetentionSchedule retention = new RetentionSchedule();

    @Data
    public static class RetentionSchedule {
        private String cron = "0 0 2 * * *";
    }
}