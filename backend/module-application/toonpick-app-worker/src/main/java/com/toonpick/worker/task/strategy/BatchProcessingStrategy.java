package com.toonpick.worker.task.strategy;

import java.util.List;

/**
 * 배치 처리 전략 인터페이스
 */
public interface BatchProcessingStrategy {
    
    /**
     * 배치 단위로 데이터를 처리합니다.
     * 
     * @param items 처리할 아이템 목록
     */
    void processBatch(List<?> items);
    
    /**
     * 배치 단위로 데이터를 처리하고 결과를 반환합니다.
     * 
     * @param items 처리할 아이템 목록
     * @return 배치 처리 결과
     */
    default BatchProcessingResult processBatchWithResult(List<?> items) {
        processBatch(items);
        // 기본 구현: 성공으로 간주하고 아이템 수를 반환
        return BatchProcessingResult.builder()
                .success(true)
                .message("배치 처리가 완료되었습니다.")
                .processedCount(items != null ? items.size() : 0)
                .totalBatchCount(1)
                .successBatchCount(1)
                .failedBatchCount(0)
                .processingTime(0)
                .build();
    }
    
    /**
     * 배치 크기를 반환합니다.
     * 
     * @return 배치 크기
     */
    int getBatchSize();
    
    /**
     * 전략의 이름을 반환합니다.
     * 
     * @return 전략 이름
     */
    String getStrategyName();
    
    /**
     * 배치 처리 전략이 지원하는 데이터 타입을 반환합니다.
     * 
     * @return 지원하는 데이터 타입
     */
    Class<?> getSupportedDataType();
} 