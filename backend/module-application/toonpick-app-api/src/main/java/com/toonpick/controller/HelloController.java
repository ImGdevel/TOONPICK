package com.toonpick.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HelloController {

    final private Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/api/public/ping")
    private ResponseEntity<Map<String, Object>> testPing() {
        Map<String, Object> response = new HashMap<>();

        response.put("status", "success");
        response.put("message", "TOONPICK 서비스가 정상적으로 작동 중입니다.");
        response.put("timestamp", LocalDateTime.now());

        logger.info("Ping 요청이 들어왔습니다. 응답: {}", response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/public/ping2")
    private ResponseEntity<Map<String, Object>> testPing2() {
        Map<String, Object> response = new HashMap<>();

        response.put("status", "success");
        response.put("message", "TOONPICK 서비스가 정상적으로 작동 중입니다. 2");
        response.put("timestamp", LocalDateTime.now());

        logger.info("Ping 요청이 들어왔습니다. 2 응답: {}", response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
