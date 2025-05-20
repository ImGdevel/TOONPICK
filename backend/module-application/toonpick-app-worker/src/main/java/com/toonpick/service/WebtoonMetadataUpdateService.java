package com.toonpick.service;

import com.toonpick.dto.request.WebtoonUpdatePayload;
import com.toonpick.entity.Webtoon;
import com.toonpick.enums.SerializationStatus;
import com.toonpick.mapper.WebtoonMapper;
import com.toonpick.publisher.WebtoonUpdatePublisher;
import com.toonpick.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 업데이트 대상 웹툰 조회 후 크롤러에 전송
 */
@Service
@RequiredArgsConstructor
public class WebtoonMetadataUpdateService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonUpdatePublisher webtoonUpdatePublisher;
    private final WebtoonMapper webtoonMapper;

    private static final int BATCH_SIZE = 20;

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
        List<WebtoonUpdatePayload> payloads = webtoonsToUpdate.stream()
                .map(webtoonMapper::toWebtoonUpdatePayload)
                .toList();

        // Batch 단위로 전송
        for (int i = 0; i < payloads.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, payloads.size());
            List<WebtoonUpdatePayload> batch = payloads.subList(i, end);
            webtoonUpdatePublisher.publishRequests(batch);
        }
    }

    public void dispatchNewWebtoonUpdateRequests() {
        // 새로운 웹툰 업데이트 요청
    }
}
