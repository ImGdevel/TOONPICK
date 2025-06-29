package com.toonpick.worker.task.strategy.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.strategy.TaskContext;
import com.toonpick.worker.task.strategy.TaskExecutionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 웹툰 에피소드 업데이트 실행 전략
 * 웹툰 에피소드 정보를 업데이트하는 작업을 실행하는 전략
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebtoonEpisodeUpdateExecutionStrategy implements TaskExecutionStrategy {
    
    @Override
    public void execute(TaskContext context) {
        log.info("웹툰 에피소드 업데이트 실행 전략 시작: {}", context.getTaskId());
        
        try {
            // 웹툰 에피소드 업데이트 로직 실행
            // 1. 새로운 에피소드 정보 수집
            // 2. 에피소드 정보 업데이트
            // 3. 관련 통계 업데이트
            
            log.info("웹툰 에피소드 업데이트 실행 전략 완료: {}", context.getTaskId());
            
        } catch (Exception e) {
            log.error("웹툰 에피소드 업데이트 실행 전략 실패: {}", context.getTaskId(), e);
            throw e;
        }
    }
    
    @Override
    public TaskType getSupportedTaskType() {
        return TaskType.WEBTOON_EPISODE_UPDATE;
    }
    
    @Override
    public String getStrategyName() {
        return "WEBTOON_EPISODE_UPDATE_EXECUTION";
    }
} 