package com.toonpick.worker.task.coordinator;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.executor.ExecutorFactory;
import com.toonpick.worker.task.executor.TaskExecutor;
import com.toonpick.worker.task.strategy.TaskContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 기본 작업 조정자
 * 작업의 전체적인 흐름을 조정하고 관리하는 기본 구현체
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultTaskCoordinator implements TaskCoordinator {
    
    private final ExecutorFactory executorFactory;
    
    @Override
    public void coordinate(TaskType taskType, TaskContext context) {
        if (!validate(taskType, context)) {
            log.error("작업 조정 검증 실패: taskType={}, context={}", taskType, context);
            throw new IllegalArgumentException("Invalid task coordination parameters");
        }
        
        log.info("작업 조정 시작: taskType={}, taskId={}", taskType, context.getTaskId());
        
        try {
            // 해당 작업 타입을 지원하는 실행자 가져오기
            TaskExecutor executor = executorFactory.getExecutor(taskType);
            
            // 실행자에게 작업 위임
            executor.execute(context);
            
            // 정리 작업 수행
            cleanup(taskType, context);
            
            log.info("작업 조정 완료: taskType={}, taskId={}", taskType, context.getTaskId());
            
        } catch (Exception e) {
            log.error("작업 조정 실패: taskType={}, taskId={}", taskType, context.getTaskId(), e);
            throw e;
        }
    }
    
    @Override
    public boolean validate(TaskType taskType, TaskContext context) {
        if (taskType == null) {
            log.error("TaskType이 null입니다.");
            return false;
        }
        
        if (context == null) {
            log.error("TaskContext가 null입니다.");
            return false;
        }
        
        if (context.getTaskId() == null) {
            log.error("TaskId가 null입니다.");
            return false;
        }
        
        if (!executorFactory.supportsTaskType(taskType)) {
            log.error("지원하지 않는 TaskType: {}", taskType);
            return false;
        }
        
        return true;
    }
    
    @Override
    public void cleanup(TaskType taskType, TaskContext context) {
        log.debug("작업 정리 수행: taskType={}, taskId={}", taskType, context.getTaskId());
        
        // 여기에 정리 작업 로직 추가
        // 예: 리소스 해제, 로그 정리, 메트릭 업데이트 등
        
        try {
            // 메모리 정리
            System.gc();
            
            // 로그 정리 (필요시)
            // logCleanupService.cleanupOldLogs();
            
            log.debug("작업 정리 완료: taskType={}, taskId={}", taskType, context.getTaskId());
            
        } catch (Exception e) {
            log.warn("작업 정리 중 오류 발생: taskType={}, taskId={}", taskType, context.getTaskId(), e);
            // 정리 작업 실패는 전체 작업 실패로 처리하지 않음
        }
    }
    
    @Override
    public String getCoordinatorName() {
        return "DefaultTaskCoordinator";
    }
} 