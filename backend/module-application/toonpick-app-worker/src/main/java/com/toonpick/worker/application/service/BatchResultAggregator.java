package com.toonpick.worker.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 배치 결과를 집계하는 컴포넌트
 */
@Slf4j
@Component
public class BatchResultAggregator {
    
    /**
     * 배치 결과들을 집계합니다.
     * 
     * @param batchResults 배치 결과 리스트
     * @param processingTime 처리 소요 시간
     * @return 집계된 처리 결과
     */
    public BatchProcessingResult aggregateResults(List<BatchResult> batchResults, long processingTime) {
        int totalProcessedCount = 0;
        int successBatchCount = 0;
        int failedBatchCount = 0;
        
        for (BatchResult result : batchResults) {
            totalProcessedCount += result.getProcessedCount();
            if (result.isSuccess()) {
                successBatchCount++;
            } else {
                failedBatchCount++;
            }
        }
        
        int totalBatchCount = batchResults.size();
        boolean isSuccess = failedBatchCount == 0;
        String message = createResultMessage(isSuccess, totalProcessedCount, totalBatchCount, failedBatchCount, processingTime);
        
        return BatchProcessingResult.builder()
                .success(isSuccess)
                .message(message)
                .processedCount(totalProcessedCount)
                .totalBatchCount(totalBatchCount)
                .successBatchCount(successBatchCount)
                .failedBatchCount(failedBatchCount)
                .processingTime(processingTime)
                .build();
    }
    
    /**
     * 결과 메시지를 생성합니다.
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