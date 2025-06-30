package com.toonpick.worker.task.executor;

import com.toonpick.worker.common.type.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 작업 실행자 팩토리
 * 작업 타입에 따른 적절한 실행자를 생성하고 관리하는 팩토리
 */
@Slf4j
@Component
public class ExecutorFactory {
    
    private final List<TaskExecutor> taskExecutors;
    private final Map<TaskType, TaskExecutor> executorMap;
    
    public ExecutorFactory(List<TaskExecutor> taskExecutors) {
        this.taskExecutors = taskExecutors;
        this.executorMap = taskExecutors.stream()
                .collect(Collectors.toMap(
                        TaskExecutor::getTaskType,
                        Function.identity()
                ));
    }
    
    /**
     * 특정 작업 타입을 지원하는 실행자를 반환합니다.
     * 
     * @param taskType 작업 타입
     * @return 해당 작업 타입을 지원하는 실행자
     */
    public TaskExecutor getExecutor(TaskType taskType) {
        // 1. 정확한 매칭 시도
        TaskExecutor executor = executorMap.get(taskType);
        if (executor != null) {
            return executor;
        }
        
        // 2. 도메인별 Executor에서 지원하는지 확인
        for (TaskExecutor taskExecutor : taskExecutors) {
            if (taskExecutor.supportsTaskType(taskType)) {
                return taskExecutor;
            }
        }
        
        log.error("지원하지 않는 작업 타입: {}", taskType);
        throw new IllegalArgumentException("지원하지 않는 작업 타입: " + taskType);
    }
    
    /**
     * 특정 작업 타입을 지원하는지 확인합니다.
     * 
     * @param taskType 작업 타입
     * @return 지원 여부
     */
    public boolean supportsTaskType(TaskType taskType) {
        // 1. 정확한 매칭 확인
        if (executorMap.containsKey(taskType)) {
            return true;
        }
        
        // 2. 도메인별 Executor에서 지원하는지 확인
        return taskExecutors.stream()
                .anyMatch(executor -> executor.supportsTaskType(taskType));
    }
    
    /**
     * 지원하는 모든 작업 타입을 반환합니다.
     * 
     * @return 지원하는 작업 타입 목록
     */
    public List<TaskType> getSupportedTaskTypes() {
        return taskExecutors.stream()
                .map(TaskExecutor::getTaskType)
                .distinct()
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 도메인의 작업 타입들을 반환합니다.
     * 
     * @param domain 도메인명
     * @return 해당 도메인의 작업 타입 목록
     */
    public List<TaskType> getTaskTypesByDomain(String domain) {
        return taskExecutors.stream()
                .filter(executor -> {
                    TaskType supportedType = executor.getTaskType();
                    return supportedType.getDomain().equals(domain);
                })
                .map(TaskExecutor::getTaskType)
                .collect(Collectors.toList());
    }
} 