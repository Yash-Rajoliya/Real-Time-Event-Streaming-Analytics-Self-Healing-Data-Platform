package com.platform.audit.util;

import java.util.UUID;

public class AuditUtils {

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}