package com.toonpick.worker.task.strategy.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.strategy.TaskContext;
import com.toonpick.worker.task.strategy.TaskExecutionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 통계 집계 실행 전략
 * 웹툰 통계 정보를 집계하는 작업을 실행하는 전략
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsAggregationExecutionStrategy implements TaskExecutionStrategy {
    
    @Override
    public void execute(TaskContext context) {
        log.info("통계 집계 실행 전략 시작: {}", context.getTaskId());
        
        try {
            // 통계 집계 로직 실행
            // 1. 조회수, 리뷰 수 등 통계 데이터 수집
            // 2. 통계 계산 및 집계
            // 3. 캐시 업데이트
            
            log.info("통계 집계 실행 전략 완료: {}", context.getTaskId());
            
        } catch (Exception e) {
            log.error("통계 집계 실행 전략 실패: {}", context.getTaskId(), e);
            throw e;
        }
    }
    
    @Override
    public TaskType getSupportedTaskType() {
        return TaskType.STATISTICS_AGGREGATION;
    }
    
    @Override
    public String getStrategyName() {
        return "STATISTICS_AGGREGATION_EXECUTION";
    }
} 