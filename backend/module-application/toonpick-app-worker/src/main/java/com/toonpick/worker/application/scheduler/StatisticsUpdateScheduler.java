package com.toonpick.worker.application.scheduler;

import com.toonpick.internal.webhook.slack.annotation.NotifyJobResult;
import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.coordinator.TaskCoordinator;
import com.toonpick.worker.task.context.TaskContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 통계 업데이트 스케줄러
 * 정기적으로 통계 집계 작업을 실행하는 스케줄러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsUpdateScheduler {

    private final TaskCoordinator taskCoordinator;

    /**
     * 매일 자정에 통계 집계 작업 실행
     */
    @NotifyJobResult(jobName = "TOONPICK worker - statistics aggregation job")
    @Scheduled(cron = "0 0 0 * * ?")
    public void aggregateStatistics() {
        log.info("일일 통계 집계 작업 시작");
        
        String taskId = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("scheduled", true);
        data.put("scheduleType", "DAILY");
        
        TaskContext context = TaskContext.builder()
                .taskId(taskId)
                .taskType(TaskType.STATISTICS_AGGREGATION)
                .data(data)
                .build();
        
        try {
            taskCoordinator.coordinate(TaskType.STATISTICS_AGGREGATION, context);
            log.info("일일 통계 집계 작업 완료: taskId={}", taskId);
        } catch (Exception e) {
            log.error("일일 통계 집계 작업 실패: taskId={}", taskId, e);
            throw e;
        }
    }
} 