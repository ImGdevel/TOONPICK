package com.toonpick.worker.task.executor;

import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.dto.payload.WebtoonCrawItem;
import com.toonpick.worker.dto.payload.WebtoonEpisodeCrawItem;
import com.toonpick.worker.infrastructure.messaging.publisher.WebtoonUpdatePublisher;
import com.toonpick.worker.mapper.WebtoonCrawItemMapper;
import com.toonpick.worker.task.strategy.StrategyFactory;
import com.toonpick.worker.task.strategy.TaskContext;
import com.toonpick.worker.task.strategy.TaskExecutionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 웹툰 업데이트 비즈니스 실행자
 * 웹툰 정보 업데이트 작업을 실행하는 클래스 (실제 비즈니스 로직 포함)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebtoonUpdateExecutor implements TaskExecutor {

    private final StrategyFactory strategyFactory;
    private final WebtoonRepository webtoonRepository;
    private final WebtoonUpdatePublisher webtoonUpdatePublisher;
    private final WebtoonCrawItemMapper webtoonCrawItemMapper;

    private static final int BATCH_SIZE = 20;

    @Override
    public void execute(TaskContext context) {
        if (!validate(context)) {
            log.error("웹툰 업데이트 작업 검증 실패: {}", context);
            throw new IllegalArgumentException("Invalid task context");
        }

        log.info("웹툰 업데이트 작업 시작: {}", context.getTaskId());

        try {
            // 웹툰 업데이트 실행 전략 가져오기
            TaskExecutionStrategy strategy = strategyFactory.getTaskExecutionStrategy(TaskType.WEBTOON_UPDATE);

            // 전략 실행
            strategy.execute(context);

            // 기존 로직: 연재 중인 웹툰 업데이트 요청 전송
            dispatchOngoingUpdateRequests();

            // 기존 로직: 에피소드 업데이트 요청 전송
            dispatchEpisodeUpdateRequests();

            // 기존 로직: 신규 웹툰 탐색 요청 전송
            dispatchNewWebtoonUpdateRequests();

            log.info("웹툰 업데이트 작업 완료: {}", context.getTaskId());

        } catch (Exception e) {
            log.error("웹툰 업데이트 작업 실패: {}", context.getTaskId(), e);
            throw e;
        }
    }

    /**
     * 매일 연재 중인 웹툰에 대한 웹데이트 Request 전송
     */
    private void dispatchOngoingUpdateRequests() {
        List<SerializationStatus> statuses = List.of(SerializationStatus.ONGOING, SerializationStatus.HIATUS);
        LocalDate thresholdDate = LocalDate.now().minusDays(6);

        // 업데이트 대상 웹툰 조회
        List<Webtoon> webtoonsToUpdate = webtoonRepository.findWebtoonsForUpdate(statuses, thresholdDate);
        if (webtoonsToUpdate.isEmpty()) {
            log.info("업데이트 대상 웹툰이 없습니다.");
            return;
        }

        // Payload 매핑
        List<WebtoonCrawItem> payloads = webtoonsToUpdate.stream()
                .map(webtoonCrawItemMapper::toWebtoonCrawItem)
                .filter(Objects::nonNull)
                .toList();

        log.info("웹툰 업데이트 전체 요청 수: {}", payloads.size());

        // Batch 단위로 전송
        for (int i = 0; i < payloads.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, payloads.size());
            List<WebtoonCrawItem> batch = payloads.subList(i, end);
            webtoonUpdatePublisher.sendWebtoonUpdateRequest(batch);
        }
    }

    /**
     * 매일 웹툰의 최신 에피소드 업데이트 요청
     */
    private void dispatchEpisodeUpdateRequests(){
        // 조회 대상 조건 정의
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
                .map(webtoonCrawItemMapper::toWebtoonEpisodeCrawItem)
                .filter(Objects::nonNull)
                .toList();

        log.info("에피소드 업데이트 요청 수: {}", payloads.size());

        for (int i = 0; i < payloads.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, payloads.size());
            List<WebtoonEpisodeCrawItem> batch = payloads.subList(i, end);
            webtoonUpdatePublisher.sendWebtoonEpisodeUpdateRequest(batch);
        }
    }

    /**
     * 신규 웹툰 탐색 요청 전송
     */
    private void dispatchNewWebtoonUpdateRequests() {
        log.info("신규 웹툰 탐색 요청 전송");
        webtoonUpdatePublisher.sendNewWebtoonDiscoveryRequest();
    }

    @Override
    public TaskType getSupportedTaskType() {
        return TaskType.WEBTOON_UPDATE;
    }

    @Override
    public String getExecutorName() {
        return "WebtoonUpdateBusinessExecutor";
    }
} 