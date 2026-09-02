package com.platform.selfhealing.recovery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerRecovery {

    public void restartConsumer(String service) {
        log.warn("Restarting consumer for service {}", service);
        // Trigger restart (K8s / API)
    }
}