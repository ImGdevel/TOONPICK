package com.toonpick.worker.controller;

import com.toonpick.worker.dto.WebtoonTriggerRequest;
import com.toonpick.worker.dto.WebtoonTriggerResponse;
import com.toonpick.worker.service.AdminWorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/worker")
@RequiredArgsConstructor
public class AdminWorkerController {

    private final AdminWorkerService adminWorkerService;

    @PostMapping("/trigger")
    public ResponseEntity<WebtoonTriggerResponse> triggerWebtoonUpdate(@RequestBody List<WebtoonTriggerRequest> requests) {
        log.info("Admin Worker Trigger 요청 수신: {} 개의 웹툰", requests.size());
        
        int processedCount = adminWorkerService.processWebtoonTriggerRequests(requests);
        
        WebtoonTriggerResponse response = WebtoonTriggerResponse.builder()
            .message("트리거 요청이 성공적으로 처리되었습니다.")
            .processedCount(processedCount)
            .status("SUCCESS")
            .timestamp(System.currentTimeMillis())
            .build();
            
        return ResponseEntity.ok(response);
    }
} 