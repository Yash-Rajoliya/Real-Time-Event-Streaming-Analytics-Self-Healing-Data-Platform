package com.platform.notification.service;

import com.platform.notification.model.*;
import com.platform.notification.util.TemplateEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final EmailSender emailSender;
    private final SlackNotifier slackNotifier;
    private final TemplateEngine templateEngine;
    private final RetryService retryService;

    public void process(Alert alert) {

        if (alert.getType() == AlertType.EMAIL) {

            String content = templateEngine.buildEmail(
                    alert.getMessage(),
                    alert.getSeverity()
            );

            retryService.executeWithRetry(() ->
                    emailSender.send(alert.getRecipient(), content)
            );
        }

        if (alert.getType() == AlertType.SLACK) {

            String payload = templateEngine.buildSlack(alert.getMessage());

            retryService.executeWithRetry(() ->
                    slackNotifier.send(alert.getRecipient(), payload)
            );
        }
    }
}