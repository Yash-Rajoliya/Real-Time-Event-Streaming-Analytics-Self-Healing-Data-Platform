package com.platform.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailSender {

    public void send(String to, String content) {
        // Simulate SMTP send
        log.info("Sending EMAIL to {} with content {}", to, content);
    }
}