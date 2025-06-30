package com.toonpick.worker.task.executor.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.context.TaskContext;
import com.toonpick.worker.task.executor.TaskExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 웹툰 통계 정보 업데이트 작업
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsUpdateTaskExecutor implements TaskExecutor {
    

    @Override
    public void execute(TaskContext context) {
        if (!validate(context)) {
            log.error("통계 업데이트 작업 검증 실패: {}", context);
            throw new IllegalArgumentException("Invalid task context");
        }
        
        log.info("통계 업데이트 작업 시작: {}", context.getTaskId());
        
        try {

        } catch (Exception e) {
            log.error("통계 업데이트 작업 실패: {}", context.getTaskId(), e);
            throw e;
        }
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.STATISTICS_AGGREGATION;
    }
} 