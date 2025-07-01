package com.toonpick.worker.task.executor.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.infrastructure.messaging.publisher.WebtoonUpdatePublisher;
import com.toonpick.worker.task.context.TaskContext;
import com.toonpick.worker.task.executor.TaskExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 새로운 웹툰 탐색 및 크롤링 요청
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NewWebtoonUpdateTaskExecutor implements TaskExecutor {

    private final WebtoonUpdatePublisher publisher;

    @Override
    public void execute(TaskContext context) {
        // todo : 새로운 웹툰 탐색 및 크롤링 요청
        try{
            publisher.sendNewWebtoonDiscoveryRequest();
        }catch (Exception e){
            // todo : 실패시 재시작 로직 작성
        }
    }

    @Override
    public TaskType getTaskType() {
        return null;
    }
}
