package com.toonpick.worker.task.executor.impl;

import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.dto.payload.WebtoonEpisodeCrawItem;
import com.toonpick.worker.infrastructure.messaging.publisher.WebtoonUpdatePublisher;
import com.toonpick.worker.mapper.WebtoonCrawItemMapper;
import com.toonpick.worker.task.context.TaskContext;
import com.toonpick.worker.task.executor.TaskExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebtoonEpisodeUpdateTaskExecutor implements TaskExecutor {

    private final WebtoonUpdatePublisher publisher;

    private final WebtoonRepository webtoonRepository;
    private final WebtoonCrawItemMapper webtoonMapper;

    private final int BATCH_SIZE = 2;

    @Override
    public void execute(TaskContext context) {
        // todo : 특정 웹툰의 전체 에피소드를 가져오도록 요청합니다.

        try{
            List<SerializationStatus> statuses = List.of(SerializationStatus.ONGOING, SerializationStatus.HIATUS);
            LocalDateTime now = LocalDateTime.now();
            DayOfWeek today = now.getDayOfWeek();
            DayOfWeek nextDay = today.plus(1);
            LocalDate threshold = LocalDate.from(now.minusDays(2));

            // 업데이트 대상 웹툰 조회
            List<Webtoon> webtoonsToUpdate = webtoonRepository.findWebtoonsForEpisodeUpdate(statuses, today, nextDay, threshold);
            if (webtoonsToUpdate.isEmpty()) {
                log.info("에피소드 업데이트 대상 웹툰이 없습니다.");
                return;
            }

            // Payload 매핑
            List<WebtoonEpisodeCrawItem> payloads = webtoonsToUpdate.stream()
                    .map(webtoonMapper::toWebtoonEpisodeCrawItem)
                    .filter(Objects::nonNull)
                    .toList();

            log.info("에피소드 업데이트 요청 수: {}", payloads.size());

            for (int i = 0; i < payloads.size(); i += BATCH_SIZE) {
                int end = Math.min(i + BATCH_SIZE, payloads.size());
                List<WebtoonEpisodeCrawItem> batch = payloads.subList(i, end);
                publisher.sendWebtoonEpisodeUpdateRequest(batch);
            }

        }catch (Exception e){
            // todo : 작업 실패시 재시도 로직 작성
        }
    }



    @Override
    public TaskType getTaskType() {
        return TaskType.WEBTOON_EPISODE_UPDATE;
    }

}
