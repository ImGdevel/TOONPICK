package com.toonpick.worker.service;

import com.toonpick.common.exception.BadRequestException;
import com.toonpick.worker.dto.WebtoonTriggerRequest;
import com.toonpick.worker.dto.payload.WebtoonCrawItem;
import com.toonpick.worker.publisher.WebtoonUpdatePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminWorkerService {

    private final WebtoonUpdatePublisher webtoonUpdatePublisher;

    /**
     * 웹툰 등록 처리
     */
    public int processWebtoonTriggerRequests(List<WebtoonTriggerRequest> requests) {
        log.info("웹툰 트리거 요청 처리 시작: {} 개의 웹툰", requests.size());

        List<WebtoonCrawItem> webtoonCrawItems = requests.stream()
                .map(this::mapToWebtoonCrawItem)
                .collect(Collectors.toList());

        webtoonUpdatePublisher.sendWebtoonUpdateRequest(webtoonCrawItems);
        
        log.info("SQS 메시지 전송 완료: {} 개의 웹툰", webtoonCrawItems.size());
        
        return webtoonCrawItems.size();
    }

    /**
     * 단일 WebtoonTriggerRequest를 WebtoonCrawItem으로 매핑합니다.
     */
    private WebtoonCrawItem mapToWebtoonCrawItem(WebtoonTriggerRequest request) {
        try {
            return WebtoonCrawItem.builder()
                .id(Long.valueOf(request.getId()))
                .platform(request.getPlatform())
                .url(request.getWebtoon_url())
                .build();
        } catch (NumberFormatException e) {
            log.error("잘못된 ID 형식: {}", request.getId());
            throw new BadRequestException("잘못된 ID 형식: " + request.getId());
        }
    }
} 