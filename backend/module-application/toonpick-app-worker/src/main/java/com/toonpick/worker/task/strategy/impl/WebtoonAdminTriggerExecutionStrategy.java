package com.toonpick.worker.task.strategy.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.strategy.TaskContext;
import com.toonpick.worker.task.strategy.TaskExecutionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 웹툰 관리자 트리거 실행 전략
 * 관리자가 수동으로 트리거한 웹툰 관련 작업을 실행하는 전략
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebtoonAdminTriggerExecutionStrategy implements TaskExecutionStrategy {
    
    @Override
    public void execute(TaskContext context) {
        log.info("웹툰 관리자 트리거 실행 전략 시작: {}", context.getTaskId());
        
        try {
            // 관리자 트리거 로직 실행
            // 1. 관리자 요청 파라미터 확인
            // 2. 요청된 작업 타입에 따른 처리
            // 3. 결과 반환
            
            log.info("웹툰 관리자 트리거 실행 전략 완료: {}", context.getTaskId());
            
        } catch (Exception e) {
            log.error("웹툰 관리자 트리거 실행 전략 실패: {}", context.getTaskId(), e);
            throw e;
        }
    }
    
    @Override
    public TaskType getSupportedTaskType() {
        return TaskType.WEBTOON_ADMIN_TRIGGER;
    }
    
    @Override
    public String getStrategyName() {
        return "WEBTOON_ADMIN_TRIGGER_EXECUTION";
    }
} 