package com.toonpick.worker.controller;

import com.toonpick.worker.dto.WebtoonTriggerRequest;
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

    @PostMapping("/trigger")
    public ResponseEntity<String> triggerWebtoonUpdate(@RequestBody List<WebtoonTriggerRequest> requests) {
        log.info("Admin Worker Trigger 요청 수신: {} 개의 웹툰", requests.size());
        
        // Test 용 로그
        for (int i = 0; i < requests.size(); i++) {
            WebtoonTriggerRequest request = requests.get(i);
            log.info("웹툰 {}: id={}, url={}, platform={}", 
                i + 1, 
                request.getId(), 
                request.getWebtoon_url(), 
                request.getPlatform());
        }
        
        return ResponseEntity.ok("트리거 요청이 성공적으로 처리되었습니다. 총 " + requests.size() + "개의 웹툰이 요청되었습니다.");
    }
} 