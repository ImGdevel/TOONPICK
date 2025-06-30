package com.toonpick.worker.task.executor.impl;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.context.TaskContext;
import com.toonpick.worker.task.executor.TaskExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 웹툰 정기 업데이트 로직
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebtoonUpdateTaskExecutor implements TaskExecutor {

    @Override
    public void execute(TaskContext context) {
        // todo : 업데이트 로직 작성
        
        // todo : 1. 업데이트 대상 웹툰 조회
        // todo : 2. 대상 웹툰은 SQS publisher를 통해 업데이트 요청 전송

        try{


        }catch (Exception e){
            // todo : 실패 작업 재실행 로직
        }

        
    }


    @Override
    public TaskType getTaskType() {
        return TaskType.WEBTOON_UPDATE;
    }

}
