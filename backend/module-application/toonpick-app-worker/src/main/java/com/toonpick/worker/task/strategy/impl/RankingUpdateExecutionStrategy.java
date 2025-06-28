package com.toonpick.worker.task.strategy.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.strategy.TaskExecutionStrategy;
import com.toonpick.worker.task.strategy.TaskContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 랭킹 업데이트 실행 전략
 * 웹툰 인기 랭킹을 계산하고 업데이트하는 작업을 실행하는 전략
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RankingUpdateExecutionStrategy implements TaskExecutionStrategy {
    
    @Override
    public void execute(TaskContext context) {
        log.info("랭킹 업데이트 실행 전략 시작: {}", context.getTaskId());
        
        try {
            // 랭킹 업데이트 로직 실행
            // 1. 조회수, 리뷰 수 등 랭킹 요소 수집
            // 2. 랭킹 알고리즘 적용
            // 3. 랭킹 정보 업데이트
            
            log.info("랭킹 업데이트 실행 전략 완료: {}", context.getTaskId());
            
        } catch (Exception e) {
            log.error("랭킹 업데이트 실행 전략 실패: {}", context.getTaskId(), e);
            throw e;
        }
    }
    
    @Override
    public TaskType getSupportedTaskType() {
        return TaskType.RANKING_UPDATE;
    }
    
    @Override
    public String getStrategyName() {
        return "RANKING_UPDATE_EXECUTION";
    }
} 