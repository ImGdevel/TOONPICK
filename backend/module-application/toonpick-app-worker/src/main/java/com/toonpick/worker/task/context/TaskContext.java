package com.toonpick.worker.task.context;

import com.toonpick.worker.common.type.TaskType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 작업 실행 컨텍스트
 */
@Getter
@Setter
@Builder
public class TaskContext {

    private String taskId;

    private TaskType taskType;

    private LocalDateTime startTime;

    private Map<String, Object> parameters; // 작업 파라미터

    private Object data; // 작업 데이터

    private Integer batchSize;

    private Integer retryCount;
} 