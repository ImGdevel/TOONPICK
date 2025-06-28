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
 * 에피소드 업데이트 스케줄러
 * 정기적으로 에피소드 업데이트 작업을 실행하는 스케줄러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EpisodeUpdateScheduler {

    private final TaskCoordinator taskCoordinator;

    /**
     * 매시간 에피소드 업데이트 작업 실행
     */
    @NotifyJobResult(jobName = "TOONPICK worker - episode update job")
    @Scheduled(cron = "0 0 * * * ?")
    public void updateEpisodes() {
        log.info("시간별 에피소드 업데이트 작업 시작");
        
        String taskId = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("scheduled", true);
        data.put("scheduleType", "HOURLY");
        
        TaskContext context = TaskContext.builder()
                .taskId(taskId)
                .taskType(TaskType.WEBTOON_EPISODE_UPDATE)
                .data(data)
                .build();
        
        try {
            taskCoordinator.coordinate(TaskType.WEBTOON_EPISODE_UPDATE, context);
            log.info("시간별 에피소드 업데이트 작업 완료: taskId={}", taskId);
        } catch (Exception e) {
            log.error("시간별 에피소드 업데이트 작업 실패: taskId={}", taskId, e);
            throw e;
        }
    }
} 