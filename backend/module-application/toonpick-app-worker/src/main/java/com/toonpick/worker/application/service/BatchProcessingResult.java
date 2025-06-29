package com.toonpick.worker.application.service;

import lombok.Builder;
import lombok.Getter;

/**
 * 전체 배치 처리 결과
 */
@Getter
@Builder
public class BatchProcessingResult {
    
    private final boolean success;
    private final String message;
    private final int processedCount;
    private final int totalBatchCount;
    private final int successBatchCount;
    private final int failedBatchCount;
    private final long processingTime;
} 