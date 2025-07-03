package com.toonpick.worker.task.executor.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.context.TaskContext;
import com.toonpick.worker.task.executor.TaskExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 웹툰 추천 알고리즘 및 추천 데이터 업데이트 작업
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationUpdateTaskExecutor implements TaskExecutor {

    @Override
    public void execute(TaskContext context) {

    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.RECOMMENDATION_UPDATE;
    }
} 