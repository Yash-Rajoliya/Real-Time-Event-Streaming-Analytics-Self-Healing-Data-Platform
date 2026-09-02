package com.platform.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RetryService {

    public void executeWithRetry(Runnable task) {

        int attempts = 3;

        for (int i = 0; i < attempts; i++) {
            try {
                task.run();
                return;
            } catch (Exception e) {
                log.error("Retry attempt {} failed", i);
            }
        }

        log.error("All retries failed");
    }
}