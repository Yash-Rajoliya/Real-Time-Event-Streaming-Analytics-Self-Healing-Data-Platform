package com.platform.config.repository;

import com.platform.config.model.AppConfig;
import org.springframework.stereotype.Repository;

@Repository
public class ConfigRepository {

    public AppConfig find(String service, String env, String key) {

        // Simulated config store
        return AppConfig.builder()
                .serviceName(service)
                .environment(env)
                .key(key)
                .value("sample-value")
                .version(1)
                .build();
    }
}