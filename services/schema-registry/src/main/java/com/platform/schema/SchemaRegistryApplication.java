// services/schema-registry/src/main/java/com/platform/schema/SchemaRegistryApplication.java
package com.platform.schema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchemaRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchemaRegistryApplication.class, args);
    }
}