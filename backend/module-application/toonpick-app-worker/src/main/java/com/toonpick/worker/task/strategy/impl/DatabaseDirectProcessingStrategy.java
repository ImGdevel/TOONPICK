package com.toonpick.worker.task.strategy.impl;

import com.toonpick.worker.task.strategy.DataProcessingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 데이터베이스 직접 처리 전략
 * 데이터를 데이터베이스에 직접 저장/업데이트하는 전략
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseDirectProcessingStrategy implements DataProcessingStrategy {
    
    @Override
    public void process(List<?> data) {
        if (data == null || data.isEmpty()) {
            log.warn("처리할 데이터가 없습니다.");
            return;
        }
        
        log.info("데이터베이스 직접 처리 시작: {} 개의 아이템", data.size());
        
        // 실제 구현에서는 JPA Repository를 주입받아 처리
        // 여기서는 로깅만 수행
        for (Object item : data) {
            log.debug("데이터베이스 처리: {}", item.getClass().getSimpleName());
            // 실제 데이터베이스 저장/업데이트 로직
        }
        
        log.info("데이터베이스 직접 처리 완료: {} 개의 아이템", data.size());
    }
    
    @Override
    public String getStrategyName() {
        return "DATABASE_DIRECT_PROCESSING";
    }
    
    @Override
    public Class<?> getSupportedDataType() {
        return Object.class; // 모든 타입 지원
    }
} 