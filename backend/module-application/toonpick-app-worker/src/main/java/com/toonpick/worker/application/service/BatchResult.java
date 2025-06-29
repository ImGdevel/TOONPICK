package com.toonpick.worker.application.service;

import lombok.Builder;
import lombok.Getter;

/**
 * 단일 배치의 처리 결과
 */
@Getter
@Builder
public class BatchResult {
    
    private final boolean success;
    private final int processedCount;
    private final String errorMessage;
    
    /**
     * 성공한 배치 결과를 생성합니다.
     */
    public static BatchResult success(int processedCount) {
        return BatchResult.builder()
                .success(true)
                .processedCount(processedCount)
                .errorMessage(null)
                .build();
    }
    
    /**
     * 실패한 배치 결과를 생성합니다.
     */
    public static BatchResult failure(int processedCount, String errorMessage) {
        return BatchResult.builder()
                .success(false)
                .processedCount(processedCount)
                .errorMessage(errorMessage)
                .build();
    }
} 