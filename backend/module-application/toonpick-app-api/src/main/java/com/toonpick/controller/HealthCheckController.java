package com.toonpick.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthCheckController {

    final private Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    @GetMapping("/api/public/health")
    public Health health() {
        logger.info("health check 요청이 들어왔습니다.");

        return Health.up()
                     .withDetail("status", "TOONPICK 서비스가 정상적으로 작동 중입니다.")
                     .build();
    }
}
