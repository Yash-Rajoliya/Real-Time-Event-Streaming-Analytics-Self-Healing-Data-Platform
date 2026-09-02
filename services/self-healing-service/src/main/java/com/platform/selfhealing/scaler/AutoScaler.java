package com.platform.selfhealing.scaler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AutoScaler {

    public void scaleUp(String service) {
        log.warn("Scaling UP service {}", service);
    }

    public void scaleDown(String service) {
        log.warn("Scaling DOWN service {}", service);
    }
}