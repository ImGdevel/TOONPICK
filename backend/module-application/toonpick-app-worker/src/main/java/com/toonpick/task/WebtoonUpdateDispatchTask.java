package com.toonpick.task;

import com.toonpick.dto.command.WebtoonUpdateCommand;
import com.toonpick.entity.Webtoon;
import com.toonpick.enums.SerializationStatus;
import com.toonpick.mapper.WebtoonMapper;
import com.toonpick.publisher.WebtoonUpdatePublisher;
import com.toonpick.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 업데이트 대상 웹툰 조회 후 크롤러에 전송
 */
@Service
@RequiredArgsConstructor
public class WebtoonUpdateDispatchTask {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonUpdatePublisher webtoonUpdatePublisher;
    private final WebtoonMapper webtoonMapper;

    private static final int BATCH_SIZE = 20;

    private final Logger logger = LoggerFactory.getLogger(WebtoonUpdateDispatchTask.class);

    /**
     * 매일 연재 중인 웹툰에 대한 웹데이트 Request 전송
     */
    public void dispatchOngoingUpdateRequests() {
        List<SerializationStatus> statuses = List.of(SerializationStatus.ONGOING, SerializationStatus.HIATUS);
        LocalDate thresholdDate = LocalDate.now().minusDays(6);

        // 업데이트 대상 웹툰 조회
        List<Webtoon> webtoonsToUpdate = webtoonRepository.findWebtoonsForUpdate(statuses, thresholdDate);
        if (webtoonsToUpdate.isEmpty()) return;

        // Payload 매핑
        List<WebtoonUpdateCommand> payloads = webtoonsToUpdate.stream()
                .map(webtoonMapper::toWebtoonUpdateCommand)
                .filter(Objects::nonNull)
                .toList();

        logger.info( "전체 요청 수: {}", payloads.size());

        // Batch 단위로 전송
        for (int i = 0; i < payloads.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, payloads.size());
            List<WebtoonUpdateCommand> batch = payloads.subList(i, end);
            webtoonUpdatePublisher.sendWebtoonUpdateRequest(batch);
        }
    }

    /**
     * 매일 웹툰의 최신 에피소드 업데이트 요청
     */
    public void dispatchEpisodeUpdateRequests(){
        List<SerializationStatus> statuses = List.of(SerializationStatus.ONGOING, SerializationStatus.HIATUS);
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = now.getDayOfWeek();
        DayOfWeek nextDay = today.plus(1);
        LocalDate threshold = LocalDate.from(now.minusDays(2));

        // 업데이트 대상 웹툰 조회
        List<Webtoon> webtoonsToUpdate = webtoonRepository.findWebtoonsForEpisodeUpdate(statuses, today, nextDay, threshold);
        if (webtoonsToUpdate.isEmpty()) return;

        // Payload 매핑
        List<WebtoonUpdateCommand> payloads = webtoonsToUpdate.stream()
                .map(webtoonMapper::toWebtoonEpisodeUpdateCommand)
                .filter(Objects::nonNull)
                .toList();

        logger.info("에피소드 업데이트 요청 수: {}", payloads.size());

        for (int i = 0; i < payloads.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, payloads.size());
            List<WebtoonUpdateCommand> batch = payloads.subList(i, end);
            webtoonUpdatePublisher.sendWebtoonEpisodeUpdateRequest(batch);
        }
    }

    public void dispatchNewWebtoonUpdateRequests() {
        // 새로운 웹툰 업데이트 요청
        webtoonUpdatePublisher.sendNewWebtoonDiscoveryRequest();
    }
}
