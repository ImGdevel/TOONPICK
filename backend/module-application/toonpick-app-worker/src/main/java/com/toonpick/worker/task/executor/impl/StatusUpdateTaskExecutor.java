package com.toonpick.worker.task.executor.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.context.TaskContext;
import com.toonpick.worker.task.executor.TaskExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 웹툰 상태(완결, 연재중단 등) 업데이트 작업
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StatusUpdateTaskExecutor implements TaskExecutor {

    @Override
    public void execute(TaskContext context) {

    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.WEBTOON_STATUS_UPDATE;
    }

} 