package com.toonpick.worker.task.strategy.impl;

import com.toonpick.worker.dto.payload.WebtoonCrawItem;
import com.toonpick.worker.dto.request.WebtoonTriggerRequest;
import com.toonpick.worker.infrastructure.messaging.publisher.WebtoonUpdatePublisher;
import com.toonpick.worker.task.strategy.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 웹툰 트리거 배치 처리 전략
 * WebtoonTriggerRequest를 배치 단위로 처리하는 전략
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebtoonTriggerBatchProcessingStrategy implements BatchProcessingStrategy {
    
    private final WebtoonUpdatePublisher webtoonUpdatePublisher;
    private final RetryProcessor retryProcessor;
    private final BatchResultAggregator resultAggregator;
    
    @Value("${batch.size:5}")
    private int batchSize;
    
    @Value("${batch.delay-ms:1000}")
    private long batchDelayMs;
    
    @Override
    public void processBatch(List<?> items) {
        if (items == null || items.isEmpty()) {
            log.warn("처리할 WebtoonTriggerRequest가 없습니다.");
            return;
        }
        
        if (!(items.get(0) instanceof WebtoonTriggerRequest)) {
            log.error("지원하지 않는 아이템 타입: {}", items.get(0).getClass().getSimpleName());
            return;
        }
        
        List<WebtoonTriggerRequest> requests = (List<WebtoonTriggerRequest>) items;
        String taskId = generateTaskId();
        
        log.info("웹툰 트리거 배치 처리 시작: {} 개의 요청, 배치 크기: {}", requests.size(), batchSize);
        
        try {
            BatchProcessingResult result = processBatches(requests, taskId);
            log.info("웹툰 트리거 배치 처리 완료: taskId={}, 성공: {}/{}, 배치: {}/{}", 
                    taskId, result.getProcessedCount(), requests.size(), 
                    result.getSuccessBatchCount(), result.getTotalBatchCount());
            
        } catch (Exception e) {
            log.error("웹툰 트리거 배치 처리 중 전체 실패: taskId={}", taskId, e);
            throw e;
        }
    }
    
    /**
     * 요청 리스트를 배치로 처리합니다.
     */
    private BatchProcessingResult processBatches(List<WebtoonTriggerRequest> requests, String taskId) {
        long startTime = System.currentTimeMillis();
        
        List<List<WebtoonTriggerRequest>> batches = createBatches(requests, batchSize);
        log.info("총 {} 개의 배치로 나누어 처리합니다.", batches.size());
        
        List<BatchResult> batchResults = new ArrayList<>();
        
        for (int i = 0; i < batches.size(); i++) {
            List<WebtoonTriggerRequest> batch = batches.get(i);
            int batchNumber = i + 1;
            
            BatchResult batchResult = processBatchWithRetry(batch, taskId, batchNumber);
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
    private BatchResult processBatchWithRetry(List<WebtoonTriggerRequest> batch, String taskId, int batchNumber) {
        return retryProcessor.processWithRetry(() -> processBatch(batch, taskId, batchNumber), batchNumber);
    }
    
    /**
     * 단일 배치를 처리합니다.
     */
    private BatchResult processBatch(List<WebtoonTriggerRequest> batch, String taskId, int batchNumber) {
        log.debug("배치 {} 처리 시작: {} 개의 웹툰", batchNumber, batch.size());
        
        try {
            // WebtoonTriggerRequest를 WebtoonCrawItem으로 변환
            List<WebtoonCrawItem> webtoonCrawItems = batch.stream()
                    .map(this::mapToWebtoonCrawItem)
                    .collect(Collectors.toList());
            
            // SQS 메시지 전송
            webtoonUpdatePublisher.sendWebtoonUpdateRequest(webtoonCrawItems);
            
            log.debug("배치 {} 처리 완료: {} 개의 웹툰", batchNumber, batch.size());
            return BatchResult.success(batch.size());
            
        } catch (Exception e) {
            log.error("배치 {} 처리 실패: {}", batchNumber, e.getMessage());
            return BatchResult.failure(0, e.getMessage());
        }
    }
    
    /**
     * WebtoonTriggerRequest를 WebtoonCrawItem으로 매핑합니다.
     */
    private WebtoonCrawItem mapToWebtoonCrawItem(WebtoonTriggerRequest request) {
        try {
            return WebtoonCrawItem.builder()
                    .id(Long.valueOf(request.getId()))
                    .platform(request.getPlatform())
                    .url(request.getWebtoon_url())
                    .build();
        } catch (NumberFormatException e) {
            log.error("잘못된 ID 형식: {}", request.getId());
            throw new IllegalArgumentException("잘못된 ID 형식: " + request.getId());
        }
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
    
    /**
     * 작업 ID를 생성합니다.
     */
    private String generateTaskId() {
        return "webtoon-trigger-" + System.currentTimeMillis();
    }
    
    @Override
    public int getBatchSize() {
        return batchSize;
    }
    
    @Override
    public String getStrategyName() {
        return "WEBTOON_TRIGGER_BATCH_PROCESSING";
    }
    
    @Override
    public Class<?> getSupportedDataType() {
        return WebtoonTriggerRequest.class;
    }
    
    @Override
    public BatchProcessingResult processBatchWithResult(List<?> items) {
        if (items == null || items.isEmpty()) {
            log.warn("처리할 WebtoonTriggerRequest가 없습니다.");
            return BatchProcessingResult.builder()
                    .success(true)
                    .message("처리할 요청이 없습니다.")
                    .processedCount(0)
                    .totalBatchCount(0)
                    .successBatchCount(0)
                    .failedBatchCount(0)
                    .processingTime(0)
                    .build();
        }
        
        if (!(items.get(0) instanceof WebtoonTriggerRequest)) {
            log.error("지원하지 않는 아이템 타입: {}", items.get(0).getClass().getSimpleName());
            return BatchProcessingResult.builder()
                    .success(false)
                    .message("지원하지 않는 아이템 타입: " + items.get(0).getClass().getSimpleName())
                    .processedCount(0)
                    .totalBatchCount(0)
                    .successBatchCount(0)
                    .failedBatchCount(1)
                    .processingTime(0)
                    .build();
        }
        
        List<WebtoonTriggerRequest> requests = (List<WebtoonTriggerRequest>) items;
        String taskId = generateTaskId();
        
        log.info("웹툰 트리거 배치 처리 시작: {} 개의 요청, 배치 크기: {}", requests.size(), batchSize);
        
        try {
            BatchProcessingResult result = processBatches(requests, taskId);
            log.info("웹툰 트리거 배치 처리 완료: taskId={}, 성공: {}/{}, 배치: {}/{}", 
                    taskId, result.getProcessedCount(), requests.size(), 
                    result.getSuccessBatchCount(), result.getTotalBatchCount());
            return result;
            
        } catch (Exception e) {
            log.error("웹툰 트리거 배치 처리 중 전체 실패: taskId={}", taskId, e);
            return BatchProcessingResult.builder()
                    .success(false)
                    .message("배치 처리 중 오류 발생: " + e.getMessage())
                    .processedCount(0)
                    .totalBatchCount(0)
                    .successBatchCount(0)
                    .failedBatchCount(1)
                    .processingTime(0)
                    .build();
        }
    }
} 