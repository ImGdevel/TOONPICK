package com.toonpick.worker.task.strategy;

import com.toonpick.worker.common.type.TaskType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 전략 팩토리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StrategyFactory {
    
    private final List<TaskExecutionStrategy> taskExecutionStrategies;
    private final List<DataProcessingStrategy> dataProcessingStrategies;
    private final List<BatchProcessingStrategy> batchProcessingStrategies;
    private final List<StatusUpdateStrategy> statusUpdateStrategies;
    
    private Map<TaskType, TaskExecutionStrategy> taskExecutionStrategyMap;
    private Map<String, DataProcessingStrategy> dataProcessingStrategyMap;
    private Map<String, BatchProcessingStrategy> batchProcessingStrategyMap;
    private Map<String, StatusUpdateStrategy> statusUpdateStrategyMap;
    
    /**
     * 초기화: 전략 맵 생성
     */
    @PostConstruct
    public void initialize() {
        // TaskExecutionStrategy 맵 생성
        this.taskExecutionStrategyMap = taskExecutionStrategies.stream()
                .collect(Collectors.toMap(
                        TaskExecutionStrategy::getSupportedTaskType,
                        Function.identity(),
                        (existing, replacement) -> {
                            log.warn("중복된 TaskExecutionStrategy 발견: {}", existing.getStrategyName());
                            return existing;
                        }
                ));
        
        // DataProcessingStrategy 맵 생성
        this.dataProcessingStrategyMap = dataProcessingStrategies.stream()
                .collect(Collectors.toMap(
                        DataProcessingStrategy::getStrategyName,
                        Function.identity(),
                        (existing, replacement) -> {
                            log.warn("중복된 DataProcessingStrategy 발견: {}", existing.getStrategyName());
                            return existing;
                        }
                ));
        
        // BatchProcessingStrategy 맵 생성
        this.batchProcessingStrategyMap = batchProcessingStrategies.stream()
                .collect(Collectors.toMap(
                        BatchProcessingStrategy::getStrategyName,
                        Function.identity(),
                        (existing, replacement) -> {
                            log.warn("중복된 BatchProcessingStrategy 발견: {}", existing.getStrategyName());
                            return existing;
                        }
                ));
        
        // StatusUpdateStrategy 맵 생성
        this.statusUpdateStrategyMap = statusUpdateStrategies.stream()
                .collect(Collectors.toMap(
                        StatusUpdateStrategy::getStrategyName,
                        Function.identity(),
                        (existing, replacement) -> {
                            log.warn("중복된 StatusUpdateStrategy 발견: {}", existing.getStrategyName());
                            return existing;
                        }
                ));
        
        log.info("StrategyFactory 초기화 완료 - TaskExecution: {}, DataProcessing: {}, BatchProcessing: {}, StatusUpdate: {}", 
                taskExecutionStrategyMap.size(), dataProcessingStrategyMap.size(), 
                batchProcessingStrategyMap.size(), statusUpdateStrategyMap.size());
    }
    
    /**
     * TaskType에 따른 TaskExecutionStrategy 반환
     */
    public TaskExecutionStrategy getTaskExecutionStrategy(TaskType taskType) {
        TaskExecutionStrategy strategy = taskExecutionStrategyMap.get(taskType);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 TaskType: " + taskType);
        }
        return strategy;
    }
    
    /**
     * 전략 이름에 따른 DataProcessingStrategy 반환
     */
    public DataProcessingStrategy getDataProcessingStrategy(String strategyName) {
        DataProcessingStrategy strategy = dataProcessingStrategyMap.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 DataProcessingStrategy: " + strategyName);
        }
        return strategy;
    }
    
    /**
     * 전략 이름에 따른 BatchProcessingStrategy 반환
     */
    public BatchProcessingStrategy getBatchProcessingStrategy(String strategyName) {
        BatchProcessingStrategy strategy = batchProcessingStrategyMap.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 BatchProcessingStrategy: " + strategyName);
        }
        return strategy;
    }
    
    /**
     * 전략 이름에 따른 StatusUpdateStrategy 반환
     */
    public StatusUpdateStrategy getStatusUpdateStrategy(String strategyName) {
        StatusUpdateStrategy strategy = statusUpdateStrategyMap.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 StatusUpdateStrategy: " + strategyName);
        }
        return strategy;
    }
    
    /**
     * 특정 TaskType을 지원하는 전략이 있는지 확인
     */
    public boolean supportsTaskType(TaskType taskType) {
        return taskExecutionStrategyMap.containsKey(taskType);
    }
    
    /**
     * 특정 전략 이름을 지원하는지 확인
     */
    public boolean supportsStrategy(String strategyName, Class<?> strategyType) {
        if (strategyType == DataProcessingStrategy.class) {
            return dataProcessingStrategyMap.containsKey(strategyName);
        } else if (strategyType == BatchProcessingStrategy.class) {
            return batchProcessingStrategyMap.containsKey(strategyName);
        } else if (strategyType == StatusUpdateStrategy.class) {
            return statusUpdateStrategyMap.containsKey(strategyName);
        }
        return false;
    }
} 