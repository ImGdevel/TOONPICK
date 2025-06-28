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