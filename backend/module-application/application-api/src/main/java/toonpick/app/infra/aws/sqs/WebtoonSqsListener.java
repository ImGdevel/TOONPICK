package toonpick.app.infra.aws.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import toonpick.app.dto.webtoon.WebtoonUpdateBatchRequestDTO;
import toonpick.app.dto.webtoon.WebtoonUpdateRequestDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebtoonSqsListener {

    @SqsListener("${spring.cloud.aws.sqs.queue.response.name}")
    public void receiveMessage(WebtoonUpdateBatchRequestDTO batchRequest) {
        log.info("Received batch message from SQS: {}", batchRequest.getRequestId());

        for (WebtoonUpdateRequestDTO webtoon : batchRequest.getWebtoons()) {
            log.info("Processing webtoon update: {}", webtoon);
        }
    }
}
