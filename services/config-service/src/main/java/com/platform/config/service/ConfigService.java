package com.platform.config.service;

import com.platform.config.cache.ConfigCache;
import com.platform.config.model.*;
import com.platform.config.repository.ConfigRepository;
import com.platform.config.util.ConfigKeyBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private final ConfigRepository repository;
    private final ConfigCache cache;

    public ConfigResponse get(ConfigRequest request) {

        String cacheKey = ConfigKeyBuilder.build(
                request.getServiceName(),
                request.getEnvironment(),
                request.getKey()
        );

        AppConfig config = cache.get(cacheKey);

        if (config == null) {
            config = repository.find(
                    request.getServiceName(),
                    request.getEnvironment(),
                    request.getKey()
            );
            cache.put(cacheKey, config);
        }

        return ConfigResponse.builder()
                .key(config.getKey())
                .value(config.getValue())
                .version(config.getVersion())
                .build();
    }
}