package com.toonpick.worker.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * 재시도 로직을 담당하는 컴포넌트
 */
@Slf4j
@Component
public class RetryProcessor {
    
    @Value("${batch.retry.count:2}")
    private int maxRetryCount;
    
    @Value("${batch.retry.base-delay-ms:1000}")
    private long baseDelayMs;
    
    /**
     * 재시도 로직과 함께 작업을 실행합니다.
     * 
     * @param task 실행할 작업
     * @param batchNumber 배치 번호 (로깅용)
     * @return 배치 결과
     */
    public BatchResult processWithRetry(Supplier<BatchResult> task, int batchNumber) {
        for (int retryCount = 0; retryCount <= maxRetryCount; retryCount++) {
            try {
                BatchResult result = task.get();
                if (result.isSuccess()) {
                    return result;
                }
                // 실패한 경우 재시도
                logRetryAttempt(batchNumber, retryCount, result.getErrorMessage());
                
            } catch (Exception e) {
                String errorMessage = String.format("배치 %d 처리 중 예외 발생: %s", batchNumber, e.getMessage());
                logRetryAttempt(batchNumber, retryCount, errorMessage);
                
                if (retryCount == maxRetryCount) {
                    return BatchResult.failure(0, errorMessage);
                }
            }
            
            // 재시도 전 지연 (지수 백오프)
            if (retryCount < maxRetryCount) {
                delayBeforeRetry(retryCount);
            }
        }
        
        return BatchResult.failure(0, "최대 재시도 횟수 초과");
    }
    
    /**
     * 재시도 시도 로그를 남깁니다.
     */
    private void logRetryAttempt(int batchNumber, int retryCount, String errorMessage) {
        if (retryCount == maxRetryCount) {
            log.error("배치 {} 처리 실패 (최종): {}", batchNumber, errorMessage);
        } else {
            log.warn("배치 {} 처리 실패 (재시도 {}/{}): {} - 재시도 중...", 
                    batchNumber, retryCount + 1, maxRetryCount + 1, errorMessage);
        }
    }
    
    /**
     * 재시도 전 지연을 처리합니다.
     */
    private void delayBeforeRetry(int retryCount) {
        long delayMs = baseDelayMs * (retryCount + 1); // 지수 백오프
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("재시도 중 인터럽트 발생");
        }
    }
} 