package com.platform.ratelimiter.controller;

import com.platform.ratelimiter.model.*;
import com.platform.ratelimiter.service.RateLimiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rate-limit")
@RequiredArgsConstructor
public class RateLimiterController {

    private final RateLimiterService service;

    @PostMapping("/check")
    public RateLimitResponse check(@RequestBody RateLimitRequest request) {
        return service.check(request);
    }
}