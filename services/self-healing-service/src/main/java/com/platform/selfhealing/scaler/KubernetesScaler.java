package com.platform.selfhealing.scaler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KubernetesScaler {

    public void scaleDeployment(String deployment, int replicas) {
        log.warn("Scaling {} to {} replicas", deployment, replicas);
    }
}