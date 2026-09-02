package com.platform.audit.controller;

import com.platform.audit.model.AuditEvent;
import com.platform.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService service;

    @PostMapping
    public String log(@RequestBody AuditEvent event) {
        service.handle(event);
        return "Logged";
    }
}