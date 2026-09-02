package com.platform.notification.util;

import org.springframework.stereotype.Component;

@Component
public class TemplateEngine {

    public String buildEmail(String message, String severity) {
        return "<h3>Alert</h3><p>" + message + "</p><b>" + severity + "</b>";
    }

    public String buildSlack(String message) {
        return "{ \"text\": \"" + message + "\" }";
    }
}