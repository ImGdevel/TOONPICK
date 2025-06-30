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
 * 관리자가 웹툰 관련 작업을 트리거할 수 있는 API 엔드포인트
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/worker")
@RequiredArgsConstructor
public class AdminWorkerController {
    
    private final AdminWorkerService adminWorkerService;
    
    /**
     * 웹툰 트리거 요청을 처리합니다.
     * 
     * @param requests 웹툰 트리거 요청 리스트
     * @return 처리 결과
     */
    @PostMapping("/webtoon/trigger")
    public ResponseEntity<ApiResponse> triggerWebtoonUpdate(
            @RequestBody List<WebtoonTriggerRequest> requests) {


        return ResponseEntity.ok(ApiResponse.success());
    }
    
    /**
     * 헬스 체크 엔드포인트
     * 
     * @return 서비스 상태
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Admin Worker Service is running");
    }
} 