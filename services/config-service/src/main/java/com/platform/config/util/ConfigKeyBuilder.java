package com.platform.config.util;

public class ConfigKeyBuilder {

    public static String build(String service, String env, String key) {
        return service + ":" + env + ":" + key;
    }
}