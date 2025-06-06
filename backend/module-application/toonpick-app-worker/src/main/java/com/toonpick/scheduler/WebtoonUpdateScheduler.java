package com.toonpick.scheduler;

import com.toonpick.internal.webhook.slack.annotation.NotifyJobResult;
import com.toonpick.task.WebtoonUpdateBatchCoordinator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class WebtoonUpdateScheduler {

    final private WebtoonUpdateBatchCoordinator webtoonUpdateService;

    final private Logger logger = LoggerFactory.getLogger(WebtoonUpdateScheduler.class);

    // 매일 22:30 ~ 23:30까지 1분 간격 실행
    @NotifyJobResult(jobName = "TOONPICK worker - data update job")
    @Scheduled(cron = "0 30-59/1 22 * * *")
    @Scheduled(cron = "0 0-30/1 23 * * *")
    public void updatePeak() {
        webtoonUpdateService.executeMetadataSync();
    }

    // 매일 23:30 ~ 다음날 01:00까지 15분 간격 실행
    @NotifyJobResult(jobName = "TOONPICK worker - data update job")
    @Scheduled(cron = "0 30 23 * * *")
    @Scheduled(cron = "0 0/15 0 * * *")
    @Scheduled(cron = "0 0 1 * * *")
    public void updateOffPeak() {
        webtoonUpdateService.executeMetadataSync();
    }

    @NotifyJobResult(jobName = "TOONPICK worker - periodic data update job")
    @Scheduled(cron = "0 0/2 * * * *")
    public void updatePeriodically() {
        logger.info("작업 수행");
        webtoonUpdateService.executeMetadataSync();
    }
}
