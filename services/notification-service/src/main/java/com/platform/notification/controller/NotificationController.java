package com.platform.notification.controller;

import com.platform.notification.model.Alert;
import com.platform.notification.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final AlertService service;

    @PostMapping
    public String send(@RequestBody Alert alert) {
        service.process(alert);
        return "Sent";
    }
}