package com.platform.config.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppConfig {

    private String serviceName;
    private String environment;
    private String key;
    private String value;
    private int version;
}