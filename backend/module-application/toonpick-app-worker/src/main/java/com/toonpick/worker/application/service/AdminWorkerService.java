package com.toonpick.worker.application.service;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.dto.request.WebtoonTriggerRequest;
import com.toonpick.worker.dto.response.WebtoonTriggerResponse;
import com.toonpick.worker.task.coordinator.TaskCoordinator;
import com.toonpick.worker.task.strategy.TaskContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 관리자 Worker 서비스
 * 관리자가 요청한 작업들을 처리하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminWorkerService {
    
    private final TaskCoordinator taskCoordinator;
    private final BatchProcessor batchProcessor;
    
    /**
     * 웹툰 트리거 요청을 배치로 처리합니다.
     * 
     * @param requests 웹툰 트리거 요청 리스트
     * @return 처리 결과
     */
    public WebtoonTriggerResponse processWebtoonTriggerRequests(List<WebtoonTriggerRequest> requests) {
        String taskId = UUID.randomUUID().toString();
        log.info("웹툰 트리거 요청 배치 처리 시작: {} 개의 웹툰, taskId: {}", requests.size(), taskId);
        
        try {
            BatchProcessingResult result = batchProcessor.processBatches(requests, taskId, taskCoordinator);
            
            log.info("웹툰 트리거 요청 배치 처리 완료: taskId={}, 성공: {}/{}, 배치: {}/{}, 소요시간: {}ms", 
                    taskId, result.getProcessedCount(), requests.size(), 
                    result.getSuccessBatchCount(), result.getTotalBatchCount(), result.getProcessingTime());
            
            return createResponse(result, taskId);
            
        } catch (Exception e) {
            log.error("웹툰 트리거 요청 배치 처리 중 전체 실패: taskId={}", taskId, e);
            return createErrorResponse(taskId, e.getMessage());
        }
    }
    
    private WebtoonTriggerResponse createResponse(BatchProcessingResult result, String taskId) {
        return WebtoonTriggerResponse.builder()
                .success(result.isSuccess())
                .message(result.getMessage())
                .taskId(taskId)
                .processedCount(result.getProcessedCount())
                .timestamp(System.currentTimeMillis())
                .build();
    }
    
    private WebtoonTriggerResponse createErrorResponse(String taskId, String errorMessage) {
        return WebtoonTriggerResponse.builder()
                .success(false)
                .message("웹툰 트리거 요청 처리 중 전체 오류가 발생했습니다: " + errorMessage)
                .taskId(taskId)
                .processedCount(0)
                .timestamp(System.currentTimeMillis())
                .build();
    }
} 