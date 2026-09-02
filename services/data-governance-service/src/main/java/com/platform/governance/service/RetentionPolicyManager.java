// services/data-governance-service/src/main/java/com/platform/governance/service/RetentionPolicyManager.java
package com.platform.governance.service;

import com.platform.governance.config.GovernanceConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetentionPolicyManager {

    private final GovernanceConfig governanceConfig;

    @Scheduled(cron = "${governance.retention.cron:0 0 2 * * *}") // Runs daily at 02:00 AM
    public void enforceRetentionPolicies() {
        log.info("Starting scheduled data retention policy enforcement task...");
        Map<String, Integer> policies = governanceConfig.getRetentionDaysByDataset();

        if (policies == null || policies.isEmpty()) {
            log.info("No explicit retention policies configured.");
            return;
        }

        policies.forEach((dataset, retentionDays) -> {
            Instant cutoffDate = Instant.now().minus(retentionDays, ChronoUnit.DAYS);
            log.info("Applying policy for dataset [{}]: purging records older than [{}] days (Cutoff: {})", 
                    dataset, retentionDays, cutoffDate);
            purgeExpiredData(dataset, cutoffDate);
        });

        log.info("Completed retention policy enforcement execution.");
    }

    public boolean isRecordExpired(String dataset, Instant recordTimestamp) {
        Integer retentionDays = governanceConfig.getRetentionDaysByDataset()
                .getOrDefault(dataset, governanceConfig.getDefaultRetentionDays());

        Instant cutoffDate = Instant.now().minus(retentionDays, ChronoUnit.DAYS);
        return recordTimestamp.isBefore(cutoffDate);
    }

    private void purgeExpiredData(String dataset, Instant cutoffDate) {
        // Implementation delegates deletion queries down to target stores (Elasticsearch, PostgreSQL, etc.)
        log.debug("Purging dataset [{}] where timestamp < {}", dataset, cutoffDate);
    }
}