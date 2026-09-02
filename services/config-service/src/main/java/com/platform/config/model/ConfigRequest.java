package com.platform.config.model;

import lombok.Data;

@Data
public class ConfigRequest {
    private String serviceName;
    private String environment;
    private String key;
}