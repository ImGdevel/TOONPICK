package com.toonpick.worker.task.executor;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.context.TaskContext;

/**
 * 작업 실행자 인터페이스
 * 실제 작업을 실행하는 클래스들의 기본 인터페이스
 */
public interface TaskExecutor {
    
    /**
     * 작업을 실행합니다.
     * 
     * @param context 작업 실행 컨텍스트
     */
    void execute(TaskContext context);
    
    /**
     * 이 실행자가 지원하는 작업 타입을 반환합니다.
     * 
     * @return 지원하는 작업 타입
     */
    TaskType getTaskType();

    /**
     * 작업 실행 전 검증을 수행합니다.
     *
     * @param context 작업 실행 컨텍스트
     * @return 검증 통과 여부
     */
    default boolean validate(TaskContext context) {
        return context != null && context.getTaskId() != null;
    }

    /**
     * 특정 작업 타입을 지원하는지 확인합니다.
     * 도메인별 Executor에서 여러 작업 타입을 지원할 때 사용됩니다.
     *
     * @param taskType 확인할 작업 타입
     * @return 지원 여부
     */
    default boolean supportsTaskType(TaskType taskType) {
        return getTaskType().equals(taskType);
    }
} 