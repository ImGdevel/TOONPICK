package com.toonpick.publisher;

import com.toonpick.dto.request.WebtoonUpdateBulkMessage;
import com.toonpick.dto.request.WebtoonUpdatePayload;
import com.toonpick.service.AwsSqsPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebtoonUpdatePublisher {
    private final AwsSqsPublisher publisher;

    @Value("${spring.cloud.aws.sqs.queue.webtoon-update-request}")
    private String queueName;

    public void publishRequests(List<WebtoonUpdatePayload> webtoonUpdatePayloads) {

        WebtoonUpdateBulkMessage msg = WebtoonUpdateBulkMessage.builder()
                .size(webtoonUpdatePayloads.size())
                .requests(webtoonUpdatePayloads)
                .build();


        publisher.publisher(queueName, msg);
    }
}
