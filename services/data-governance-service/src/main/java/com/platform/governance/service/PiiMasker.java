// services/data-governance-service/src/main/java/com/platform/governance/service/PiiMasker.java
package com.platform.governance.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
public class PiiMasker {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");
    private static final Pattern SSN_PATTERN = Pattern.compile("^\\d{3}-\\d{2}-\\d{4}$");
    private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile("^\\d{4}[- ]?\\d{4}[- ]?\\d{4}[- ]?\\d{4}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{10,15}$");

    private static final List<String> SENSITIVE_KEY_KEYWORDS = List.of(
            "email", "ssn", "socialsecurity", "card", "creditcard", "phone", "password", "secret"
    );

    public Map<String, Object> maskRecord(Map<String, Object> inputRecord) {
        if (inputRecord == null) {
            return null;
        }

        Map<String, Object> maskedMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : inputRecord.entrySet()) {
            maskedMap.put(entry.getKey(), maskValue(entry.getKey(), entry.getValue()));
        }
        return maskedMap;
    }

    @SuppressWarnings("unchecked")
    public Object maskValue(String key, Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Map) {
            return maskRecord((Map<String, Object>) value);
        }

        if (value instanceof String strVal) {
            String lowercaseKey = key.toLowerCase();

            // Key-based heuristic masking
            if (lowercaseKey.contains("email") || EMAIL_PATTERN.matcher(strVal).matches()) {
                return maskEmail(strVal);
            }
            if (lowercaseKey.contains("ssn") || SSN_PATTERN.matcher(strVal).matches()) {
                return "***-**-" + (strVal.length() >= 4 ? strVal.substring(strVal.length() - 4) : "****");
            }
            if (lowercaseKey.contains("card") || CREDIT_CARD_PATTERN.matcher(strVal).matches()) {
                String digitsOnly = strVal.replaceAll("[^\\d]", "");
                return (digitsOnly.length() >= 4) 
                        ? "****-****-****-" + digitsOnly.substring(digitsOnly.length() - 4)
                        : "****-****-****-****";
            }
            if (lowercaseKey.contains("phone") || PHONE_PATTERN.matcher(strVal).matches()) {
                return strVal.replaceAll("\\d(?=\\d{4})", "*");
            }
            if (lowercaseKey.contains("password") || lowercaseKey.contains("secret")) {
                return "********";
            }
        }

        return value;
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return "***@***.**";
        }
        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex);
        return localPart.charAt(0) + "***" + localPart.charAt(localPart.length() - 1) + domainPart;
    }
}