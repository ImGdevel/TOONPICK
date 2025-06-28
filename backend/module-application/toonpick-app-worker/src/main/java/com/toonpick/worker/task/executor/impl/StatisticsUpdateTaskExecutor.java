package com.toonpick.worker.task.executor.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.executor.TaskExecutor;
import com.toonpick.worker.task.strategy.TaskContext;
import com.toonpick.worker.task.strategy.TaskExecutionStrategy;
import com.toonpick.worker.task.strategy.StrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 통계 업데이트 작업 실행자
 * 웹툰 통계 정보 업데이트 작업을 실행하는 클래스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsUpdateTaskExecutor implements TaskExecutor {
    
    private final StrategyFactory strategyFactory;
    
    @Override
    public void execute(TaskContext context) {
        if (!validate(context)) {
            log.error("통계 업데이트 작업 검증 실패: {}", context);
            throw new IllegalArgumentException("Invalid task context");
        }
        
        log.info("통계 업데이트 작업 시작: {}", context.getTaskId());
        
        try {
            // 통계 업데이트 실행 전략 가져오기
            TaskExecutionStrategy strategy = strategyFactory.getTaskExecutionStrategy(TaskType.STATISTICS_AGGREGATION);
            
            // 전략 실행
            strategy.execute(context);
            
            log.info("통계 업데이트 작업 완료: {}", context.getTaskId());
            
        } catch (Exception e) {
            log.error("통계 업데이트 작업 실패: {}", context.getTaskId(), e);
            throw e;
        }
    }
    
    @Override
    public TaskType getSupportedTaskType() {
        return TaskType.STATISTICS_AGGREGATION;
    }
    
    @Override
    public String getExecutorName() {
        return "StatisticsUpdateTaskExecutor";
    }
} 