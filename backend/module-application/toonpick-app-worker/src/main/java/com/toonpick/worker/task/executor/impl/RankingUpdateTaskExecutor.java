package com.toonpick.worker.task.executor.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.context.TaskContext;
import com.toonpick.worker.task.executor.TaskExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 웹툰 인기 랭킹 계산 및 업데이트 작업
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RankingUpdateTaskExecutor implements TaskExecutor {
    

    @Override
    public void execute(TaskContext context) {

    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.RANKING_UPDATE;
    }
} 