package com.platform.config.controller;

import com.platform.config.model.*;
import com.platform.config.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigService service;

    @PostMapping("/get")
    public ConfigResponse get(@RequestBody ConfigRequest request) {
        return service.get(request);
    }
}