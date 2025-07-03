package com.toonpick.worker.application.controller;

import com.toonpick.internal.web.response.ApiResponse;
import com.toonpick.worker.application.service.AdminWorkerService;
import com.toonpick.worker.dto.request.WebtoonTriggerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 Worker 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/worker")
@RequiredArgsConstructor
public class AdminWorkerController {
    
    private final AdminWorkerService adminWorkerService;
    
    /**
     * 웹툰 트리거 요청을 처리합니다.
     */
    @PostMapping("/webtoon/trigger")
    public ResponseEntity<ApiResponse> triggerWebtoonUpdate(
            @RequestBody List<WebtoonTriggerRequest> requests) {

        // todo : 웹툰 등록 비즈니스 로직 작성
        
        return ResponseEntity.ok(ApiResponse.success());
    }
    
    /**
     * health 체크 엔드포인트
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Admin Worker Service is running");
    }
} 