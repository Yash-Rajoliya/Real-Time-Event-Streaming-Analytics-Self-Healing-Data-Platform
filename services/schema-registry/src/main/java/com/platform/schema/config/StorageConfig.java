// services/schema-registry/src/main/java/com/platform/schema/config/StorageConfig.java
package com.platform.schema.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.platform.schema.repository")
public class StorageConfig {
    // Datasource pooling, Flyway / Liquidbase migrations, and custom transaction setup
}