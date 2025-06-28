package com.toonpick.worker.task.strategy.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.strategy.TaskExecutionStrategy;
import com.toonpick.worker.task.strategy.TaskContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 웹툰 업데이트 실행 전략
 * 웹툰 정보 업데이트 작업을 실행하는 전략
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebtoonUpdateExecutionStrategy implements TaskExecutionStrategy {
    
    @Override
    public void execute(TaskContext context) {
        log.info("웹툰 업데이트 실행 전략 시작: {}", context.getTaskId());
        
        try {
            // 웹툰 업데이트 로직 실행
            // 1. 업데이트 대상 웹툰 조회
            // 2. 데이터 매핑
            // 3. SQS 메시지 전송
            
            log.info("웹툰 업데이트 실행 전략 완료: {}", context.getTaskId());
            
        } catch (Exception e) {
            log.error("웹툰 업데이트 실행 전략 실패: {}", context.getTaskId(), e);
            throw e;
        }
    }
    
    @Override
    public TaskType getSupportedTaskType() {
        return TaskType.WEBTOON_UPDATE;
    }
    
    @Override
    public String getStrategyName() {
        return "WEBTOON_UPDATE_EXECUTION";
    }
} 