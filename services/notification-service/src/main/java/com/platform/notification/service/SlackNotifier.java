package com.platform.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SlackNotifier {

    public void send(String webhookUrl, String payload) {
        // Simulate HTTP POST
        log.info("Sending SLACK message to {} payload {}", webhookUrl, payload);
    }
}