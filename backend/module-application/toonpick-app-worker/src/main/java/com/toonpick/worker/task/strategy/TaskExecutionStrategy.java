package com.toonpick.worker.task.strategy;

import com.toonpick.worker.common.type.TaskType;

/**
 * 작업 실행 전략 인터페이스
 */
public interface TaskExecutionStrategy {
    
    /**
     * 작업을 실행합니다.
     *
     * @param context 작업 실행 컨텍스트
     */
    void execute(TaskContext context);
    
    /**
     * 이 전략이 지원하는 작업 타입을 반환합니다.
     *
     * @return 지원하는 작업 타입
     */
    TaskType getSupportedTaskType();
    
    /**
     * 전략의 이름을 반환합니다.
     *
     * @return 전략 이름
     */
    String getStrategyName();
} 