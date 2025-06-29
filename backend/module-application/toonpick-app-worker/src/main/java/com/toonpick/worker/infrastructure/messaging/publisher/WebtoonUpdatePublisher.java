package com.toonpick.worker.infrastructure.messaging.publisher;

import com.toonpick.internal.aws.sqs.service.AwsSqsPublisher;
import com.toonpick.worker.common.type.SQSEventType;
import com.toonpick.worker.dto.message.SQSRequestMessage;
import com.toonpick.worker.dto.payload.WebtoonCrawItem;
import com.toonpick.worker.dto.payload.WebtoonEpisodeCrawItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebtoonUpdatePublisher {
    private final AwsSqsPublisher sqsPublisher;

    @Value("${spring.cloud.aws.sqs.queue.webtoon-update-request}")
    private String queueName;

    /**
     * 웹툰 정보 일괄 업데이트 요청
     */
    public void sendWebtoonUpdateRequest(List<WebtoonCrawItem> updateCommands) {
        SQSRequestMessage<Object> request = SQSRequestMessage.builder()
                .eventType(SQSEventType.CRAWL_WEBTOON_ALL)
                .data(updateCommands)
                .build();

        sqsPublisher.publisher(queueName, request);
    }

    /**
     * 에피소드 신규 탐색 요청
     */
    public void sendWebtoonEpisodeUpdateRequest(List<WebtoonEpisodeCrawItem> webtoonUpdateCommands) {
        SQSRequestMessage<Object> request = SQSRequestMessage.builder()
                .eventType(SQSEventType.CRAWL_WEBTOON_EPISODE)
                .data(webtoonUpdateCommands)
                .build();
        sqsPublisher.publisher(queueName, request);
    }

    /**
     * 신규 웹툰 탐색 요청
     */
    public void sendNewWebtoonDiscoveryRequest() {
        SQSRequestMessage<Object> request = SQSRequestMessage.builder()
                .eventType(SQSEventType.CRAWL_WEBTOON_NEW)
                .build();
        sqsPublisher.publisher(queueName, request);
    }
}
