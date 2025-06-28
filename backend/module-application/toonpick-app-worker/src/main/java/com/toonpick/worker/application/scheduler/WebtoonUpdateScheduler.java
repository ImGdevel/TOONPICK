package com.toonpick.worker.application.scheduler;

import com.toonpick.internal.webhook.slack.annotation.NotifyJobResult;
import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.coordinator.TaskCoordinator;
import com.toonpick.worker.task.strategy.TaskContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 웹툰 업데이트 스케줄러
 * 정기적으로 웹툰 업데이트 작업을 실행하는 스케줄러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebtoonUpdateScheduler {

    private final TaskCoordinator taskCoordinator;

    /**
     * 피크 시간대 업데이트 (매일 22:30 ~ 23:30까지 5분 간격 실행)
     */
    @NotifyJobResult(jobName = "TOONPICK worker - peak time data update job")
    @Scheduled(cron = "0 30-59/5 22 * * *")
    @Scheduled(cron = "0 0-30/5 23 * * *")
    public void updatePeak() {
        log.info("피크 시간대 웹툰 업데이트 작업 시작");
        
        String taskId = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("scheduled", true);
        data.put("scheduleType", "PEAK_TIME");
        data.put("timeSlot", "22:30-23:30");
        
        TaskContext context = TaskContext.builder()
                .taskId(taskId)
                .taskType(TaskType.WEBTOON_UPDATE)
                .data(data)
                .build();
        
        try {
            taskCoordinator.coordinate(TaskType.WEBTOON_UPDATE, context);
            log.info("피크 시간대 웹툰 업데이트 작업 완료: taskId={}", taskId);
        } catch (Exception e) {
            log.error("피크 시간대 웹툰 업데이트 작업 실패: taskId={}", taskId, e);
            throw e;
        }
    }

    /**
     * 비피크 시간대 업데이트 (매일 23:30 ~ 다음날 01:00까지 15분 간격 실행)
     */
    @NotifyJobResult(jobName = "TOONPICK worker - off-peak time data update job")
    @Scheduled(cron = "0 30 23 * * *")
    @Scheduled(cron = "0 0/15 0 * * *")
    @Scheduled(cron = "0 0 1 * * *")
    public void updateOffPeak() {
        log.info("비피크 시간대 웹툰 업데이트 작업 시작");
        
        String taskId = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("scheduled", true);
        data.put("scheduleType", "OFF_PEAK_TIME");
        data.put("timeSlot", "23:30-01:00");
        
        TaskContext context = TaskContext.builder()
                .taskId(taskId)
                .taskType(TaskType.WEBTOON_UPDATE)
                .data(data)
                .build();
        
        try {
            taskCoordinator.coordinate(TaskType.WEBTOON_UPDATE, context);
            log.info("비피크 시간대 웹툰 업데이트 작업 완료: taskId={}", taskId);
        } catch (Exception e) {
            log.error("비피크 시간대 웹툰 업데이트 작업 실패: taskId={}", taskId, e);
            throw e;
        }
    }
}
