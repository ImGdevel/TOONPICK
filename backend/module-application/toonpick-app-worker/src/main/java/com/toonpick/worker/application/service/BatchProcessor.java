package com.toonpick.worker.application.service;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.dto.request.WebtoonTriggerRequest;
import com.toonpick.worker.task.coordinator.TaskCoordinator;
import com.toonpick.worker.task.strategy.TaskContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 배치 처리를 담당하는 컴포넌트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BatchProcessor {
    
    @Value("${batch.size:5}")
    private int batchSize;
    
    @Value("${batch.delay-ms:1000}")
    private long batchDelayMs;
    
    private final RetryProcessor retryProcessor;
    private final BatchResultAggregator resultAggregator;
    
    /**
     * 요청 리스트를 배치로 처리합니다.
     * 
     * @param requests 전체 요청 리스트
     * @param taskId 작업 ID
     * @param taskCoordinator 작업 조정자
     * @return 배치 처리 결과
     */
    public BatchProcessingResult processBatches(List<WebtoonTriggerRequest> requests, String taskId, TaskCoordinator taskCoordinator) {
        long startTime = System.currentTimeMillis();
        log.info("배치 처리 시작: {} 개의 요청, 배치 크기: {}", requests.size(), batchSize);
        
        List<List<WebtoonTriggerRequest>> batches = createBatches(requests, batchSize);
        log.info("총 {} 개의 배치로 나누어 처리합니다.", batches.size());
        
        List<BatchResult> batchResults = new ArrayList<>();
        
        for (int i = 0; i < batches.size(); i++) {
            List<WebtoonTriggerRequest> batch = batches.get(i);
            int batchNumber = i + 1;
            
            BatchResult batchResult = processBatchWithRetry(batch, taskId, batchNumber, taskCoordinator);
            batchResults.add(batchResult);
            
            logBatchResult(batchResult, batchNumber, requests.size());
            
            // 배치 간 지연 (마지막 배치 제외)
            if (i < batches.size() - 1) {
                delayBetweenBatches();
            }
        }
        
        long processingTime = System.currentTimeMillis() - startTime;
        return resultAggregator.aggregateResults(batchResults, processingTime);
    }
    
    /**
     * 요청 리스트를 배치로 나눕니다.
     */
    private List<List<WebtoonTriggerRequest>> createBatches(List<WebtoonTriggerRequest> requests, int batchSize) {
        List<List<WebtoonTriggerRequest>> batches = new ArrayList<>();
        
        for (int i = 0; i < requests.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, requests.size());
            batches.add(requests.subList(i, endIndex));
        }
        
        return batches;
    }
    
    /**
     * 단일 배치를 재시도 로직과 함께 처리합니다.
     */
    private BatchResult processBatchWithRetry(List<WebtoonTriggerRequest> batch, String taskId, int batchNumber, TaskCoordinator taskCoordinator) {
        return retryProcessor.processWithRetry(() -> processBatch(batch, taskId, batchNumber, taskCoordinator), batchNumber);
    }
    
    /**
     * 단일 배치를 처리합니다.
     */
    private BatchResult processBatch(List<WebtoonTriggerRequest> batch, String taskId, int batchNumber, TaskCoordinator taskCoordinator) {
        log.debug("배치 {} 처리 시작: {} 개의 웹툰", batchNumber, batch.size());
        
        Map<String, Object> data = new HashMap<>();
        data.put("requests", batch);
        data.put("batchNumber", batchNumber);
        
        TaskContext context = TaskContext.builder()
                .taskId(taskId + "-batch-" + batchNumber)
                .taskType(TaskType.WEBTOON_ADMIN_TRIGGER)
                .data(data)
                .build();
        
        taskCoordinator.coordinate(TaskType.WEBTOON_ADMIN_TRIGGER, context);
        
        log.debug("배치 {} 처리 완료", batchNumber);
        return BatchResult.success(batch.size());
    }
    
    /**
     * 배치 간 지연을 처리합니다.
     */
    private void delayBetweenBatches() {
        try {
            Thread.sleep(batchDelayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("배치 처리 중 인터럽트 발생");
        }
    }
    
    /**
     * 배치 결과를 로깅합니다.
     */
    private void logBatchResult(BatchResult result, int batchNumber, int totalRequests) {
        if (result.isSuccess()) {
            log.info("배치 {} 처리 완료: {} 개의 웹툰 처리됨", batchNumber, result.getProcessedCount());
        } else {
            log.warn("배치 {} 처리 실패: {} 개의 웹툰 처리 실패 - {}", 
                    batchNumber, result.getProcessedCount(), result.getErrorMessage());
        }
    }
} 