package com.toonpick.worker.task.strategy.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.strategy.TaskExecutionStrategy;
import com.toonpick.worker.task.strategy.TaskContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 추천 시스템 업데이트 실행 전략
 * 웹툰 추천 알고리즘 및 추천 데이터를 업데이트하는 작업을 실행하는 전략
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationUpdateExecutionStrategy implements TaskExecutionStrategy {
    
    @Override
    public void execute(TaskContext context) {
        log.info("추천 시스템 업데이트 실행 전략 시작: {}", context.getTaskId());
        
        try {
            // 추천 시스템 업데이트 로직 실행
            // 1. 사용자 행동 데이터 수집
            // 2. 추천 모델 재학습
            // 3. 추천 결과 업데이트
            
            log.info("추천 시스템 업데이트 실행 전략 완료: {}", context.getTaskId());
            
        } catch (Exception e) {
            log.error("추천 시스템 업데이트 실행 전략 실패: {}", context.getTaskId(), e);
            throw e;
        }
    }
    
    @Override
    public TaskType getSupportedTaskType() {
        return TaskType.RECOMMENDATION_UPDATE;
    }
    
    @Override
    public String getStrategyName() {
        return "RECOMMENDATION_UPDATE_EXECUTION";
    }
} 