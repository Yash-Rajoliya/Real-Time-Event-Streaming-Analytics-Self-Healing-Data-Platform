// services/storage-connector/src/main/java/com/platform/storage/StorageConnectorApplication.java
package com.platform.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StorageConnectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageConnectorApplication.class, args);
    }
}