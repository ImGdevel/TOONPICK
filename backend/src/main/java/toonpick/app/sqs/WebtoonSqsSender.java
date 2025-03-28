package toonpick.app.sqs;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import toonpick.app.dto.webtoon.WebtoonUpdateBatchRequestDTO;
import toonpick.app.dto.webtoon.WebtoonUpdateRequestDTO;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebtoonSqsSender {

    private final SqsTemplate sqsTemplate;

    @Value("${spring.cloud.aws.sqs.queue.request.name}")
    private String queueName;

    public void sendMessages(List<WebtoonUpdateRequestDTO> requestDTOS) {
        if (requestDTOS.isEmpty()) return;

        WebtoonUpdateBatchRequestDTO batchRequest = WebtoonUpdateBatchRequestDTO.builder()
            .requestId(UUID.randomUUID().toString())
            .totalCount(requestDTOS.size())
            .webtoons(requestDTOS)
            .build();

        try {
            sqsTemplate.send(to -> to.queue(queueName).payload(batchRequest));
            log.info("Sent batch message to SQS: {}", batchRequest.getRequestId());
        } catch (Exception e) {
            log.error("Failed to send SQS batch message", e);
        }
    }
}
