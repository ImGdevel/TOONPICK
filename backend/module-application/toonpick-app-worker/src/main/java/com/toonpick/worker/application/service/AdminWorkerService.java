package com.toonpick.worker.application.service;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.dto.request.WebtoonTriggerRequest;
import com.toonpick.worker.dto.response.WebtoonTriggerResponse;
import com.toonpick.worker.task.coordinator.TaskCoordinator;
import com.toonpick.worker.task.strategy.TaskContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    private static final int BATCH_SIZE = 5;
    private static final int MAX_RETRY_COUNT = 2;
    private static final long BATCH_DELAY_MS = 1000; // 1초 지연
    
    /**
     * 웹툰 트리거 요청을 배치로 처리합니다.
     * 배치 크기: 5개씩 처리
     * 
     * @param requests 웹툰 트리거 요청 리스트
     * @return 처리 결과
     */
    public WebtoonTriggerResponse processWebtoonTriggerRequests(List<WebtoonTriggerRequest> requests) {
        long startTime = System.currentTimeMillis();
        log.info("웹툰 트리거 요청 배치 처리 시작: {} 개의 웹툰, 배치 크기: {}", requests.size(), BATCH_SIZE);
        
        String taskId = UUID.randomUUID().toString();
        int totalProcessedCount = 0;
        int totalBatchCount = 0;
        int failedBatchCount = 0;
        List<String> errorMessages = new ArrayList<>();
        
        try {
            // 요청을 배치로 나누기
            List<List<WebtoonTriggerRequest>> batches = createBatches(requests, BATCH_SIZE);
            log.info("총 {} 개의 배치로 나누어 처리합니다.", batches.size());
            
            // 각 배치별로 처리
            for (int i = 0; i < batches.size(); i++) {
                List<WebtoonTriggerRequest> batch = batches.get(i);
                totalBatchCount++;
                
                boolean batchSuccess = processBatchWithRetry(batch, taskId, i + 1, errorMessages);
                
                if (batchSuccess) {
                    totalProcessedCount += batch.size();
                    log.info("배치 {} 처리 완료: {} 개의 웹툰 처리됨 (총 처리된 웹툰: {}/{})", 
                            i + 1, batch.size(), totalProcessedCount, requests.size());
                } else {
                    failedBatchCount++;
                    log.warn("배치 {} 처리 실패: {} 개의 웹툰 처리 실패 (총 실패 배치: {})", 
                            i + 1, batch.size(), failedBatchCount);
                }
                
                // 배치 간 지연 (마지막 배치 제외)
                if (i < batches.size() - 1) {
                    try {
                        Thread.sleep(BATCH_DELAY_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.warn("배치 처리 중 인터럽트 발생");
                        break;
                    }
                }
            }
            
            // 전체 처리 결과 반환
            long processingTime = System.currentTimeMillis() - startTime;
            boolean isSuccess = failedBatchCount == 0;
            String message = createResultMessage(isSuccess, totalProcessedCount, totalBatchCount, failedBatchCount, processingTime);
            
            log.info("웹툰 트리거 요청 배치 처리 완료: taskId={}, 성공: {}/{}, 배치: {}/{}, 소요시간: {}ms", 
                    taskId, totalProcessedCount, requests.size(), totalBatchCount - failedBatchCount, totalBatchCount, processingTime);
            
            return WebtoonTriggerResponse.builder()
                    .success(isSuccess)
                    .message(message)
                    .taskId(taskId)
                    .processedCount(totalProcessedCount)
                    .timestamp(System.currentTimeMillis())
                    .build();
                    
        } catch (Exception e) {
            long processingTime = System.currentTimeMillis() - startTime;
            log.error("웹툰 트리거 요청 배치 처리 중 전체 실패: taskId={}, 소요시간: {}ms", taskId, processingTime, e);
            
            return WebtoonTriggerResponse.builder()
                    .success(false)
                    .message("웹툰 트리거 요청 처리 중 전체 오류가 발생했습니다: " + e.getMessage())
                    .taskId(taskId)
                    .processedCount(totalProcessedCount)
                    .timestamp(System.currentTimeMillis())
                    .build();
        }
    }
    
    /**
     * 배치를 재시도 로직과 함께 처리합니다.
     * 
     * @param batch 배치 요청 리스트
     * @param taskId 작업 ID
     * @param batchNumber 배치 번호
     * @param errorMessages 에러 메시지 리스트
     * @return 처리 성공 여부
     */
    private boolean processBatchWithRetry(List<WebtoonTriggerRequest> batch, String taskId, int batchNumber, List<String> errorMessages) {
        for (int retryCount = 0; retryCount <= MAX_RETRY_COUNT; retryCount++) {
            try {
                processBatch(batch, taskId, batchNumber);
                return true;
                
            } catch (Exception e) {
                String errorMsg = String.format("배치 %d 처리 실패 (재시도 %d/%d): %s", 
                        batchNumber, retryCount + 1, MAX_RETRY_COUNT + 1, e.getMessage());
                
                if (retryCount == MAX_RETRY_COUNT) {
                    // 최대 재시도 횟수 초과
                    log.error(errorMsg, e);
                    errorMessages.add(errorMsg);
                    return false;
                } else {
                    // 재시도
                    log.warn(errorMsg + " - 재시도 중...", e);
                    try {
                        Thread.sleep(BATCH_DELAY_MS * (retryCount + 1)); // 지수 백오프
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        log.warn("재시도 중 인터럽트 발생");
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * 요청 리스트를 배치로 나눕니다.
     * 
     * @param requests 전체 요청 리스트
     * @param batchSize 배치 크기
     * @return 배치 리스트
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
     * 단일 배치를 처리합니다.
     * 
     * @param batch 배치 요청 리스트
     * @param taskId 작업 ID
     * @param batchNumber 배치 번호
     */
    private void processBatch(List<WebtoonTriggerRequest> batch, String taskId, int batchNumber) {
        log.debug("배치 {} 처리 시작: {} 개의 웹툰", batchNumber, batch.size());
        
        // 배치별 TaskContext 생성
        Map<String, Object> data = new HashMap<>();
        data.put("requests", batch);
        data.put("batchNumber", batchNumber);
        
        TaskContext context = TaskContext.builder()
                .taskId(taskId + "-batch-" + batchNumber)
                .taskType(TaskType.WEBTOON_ADMIN_TRIGGER)
                .data(data)
                .build();
        
        // TaskCoordinator를 통해 배치 작업 실행
        taskCoordinator.coordinate(TaskType.WEBTOON_ADMIN_TRIGGER, context);
        
        log.debug("배치 {} 처리 완료", batchNumber);
    }
    
    /**
     * 결과 메시지를 생성합니다.
     * 
     * @param isSuccess 전체 성공 여부
     * @param processedCount 처리된 웹툰 수
     * @param totalBatchCount 전체 배치 수
     * @param failedBatchCount 실패한 배치 수
     * @param processingTime 처리 소요 시간
     * @return 결과 메시지
     */
    private String createResultMessage(boolean isSuccess, int processedCount, int totalBatchCount, int failedBatchCount, long processingTime) {
        if (isSuccess) {
            return String.format("웹툰 트리거 요청이 성공적으로 처리되었습니다. (총 %d개 배치, %d개 웹툰, 소요시간: %dms)", 
                               totalBatchCount, processedCount, processingTime);
        } else {
            return String.format("웹툰 트리거 요청이 부분적으로 처리되었습니다. (성공: %d개, 실패: %d개 배치, 소요시간: %dms)", 
                               processedCount, failedBatchCount, processingTime);
        }
    }
} 