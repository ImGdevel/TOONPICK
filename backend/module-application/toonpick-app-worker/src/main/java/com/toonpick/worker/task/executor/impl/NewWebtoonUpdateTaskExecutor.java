package com.toonpick.worker.task.executor.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.context.TaskContext;
import com.toonpick.worker.task.executor.TaskExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 새로운 웹툰 업데이트 요청
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NewWebtoonCrawlTaskExecutor implements TaskExecutor {
    @Override
    public void execute(TaskContext context) {

    }

    @Override
    public TaskType getSupportedTaskType() {
        return null;
    }

    @Override
    public String getExecutorName() {
        return "";
    }
}
