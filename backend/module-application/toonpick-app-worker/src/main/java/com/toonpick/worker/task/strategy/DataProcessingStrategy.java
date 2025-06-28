package com.toonpick.worker.task.strategy;

import java.util.List;

/**
 * 데이터 처리 전략 인터페이스
 */
public interface DataProcessingStrategy {
    
    /**
     * 데이터를 처리합니다.
     * 
     * @param data 처리할 데이터 목록
     */
    void process(List<?> data);
    
    /**
     * 전략의 이름을 반환합니다.
     * 
     * @return 전략 이름
     */
    String getStrategyName();
    
    /**
     * 전략이 지원하는 데이터 타입을 반환합니다.
     * 
     * @return 지원하는 데이터 타입
     */
    Class<?> getSupportedDataType();
} 