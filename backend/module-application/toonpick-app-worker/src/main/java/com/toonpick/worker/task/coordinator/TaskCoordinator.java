package com.toonpick.worker.task.coordinator;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.context.TaskContext;

/**
 * 작업 조정자 인터페이스
 * 작업의 전체적인 흐름을 조정하고 관리하는 클래스들의 기본 인터페이스
 */
public interface TaskCoordinator {
    
    /**
     * 작업을 조정하고 실행합니다.
     * 
     * @param taskType 작업 타입
     * @param context 작업 컨텍스트
     */
    void coordinate(TaskType taskType, TaskContext context);
    
    /**
     * 작업 실행 전 검증을 수행합니다.
     * 
     * @param taskType 작업 타입
     * @param context 작업 컨텍스트
     * @return 검증 통과 여부
     */
    boolean validate(TaskType taskType, TaskContext context);
    
    /**
     * 작업 실행 후 정리 작업을 수행합니다.
     * 
     * @param taskType 작업 타입
     * @param context 작업 컨텍스트
     */
    void cleanup(TaskType taskType, TaskContext context);

} 