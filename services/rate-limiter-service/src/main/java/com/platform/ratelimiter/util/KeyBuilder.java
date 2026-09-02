package com.platform.ratelimiter.util;

public class KeyBuilder {

    public static String tokenKey(String clientId) {
        return "rate:tokens:" + clientId;
    }

    public static String timestampKey(String clientId) {
        return "rate:timestamp:" + clientId;
    }
}