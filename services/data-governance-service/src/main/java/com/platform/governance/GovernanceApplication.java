// services/data-governance-service/src/main/java/com/platform/governance/GovernanceApplication.java
package com.platform.governance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GovernanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GovernanceApplication.class, args);
    }
}