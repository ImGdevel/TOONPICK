package com.toonpick.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.dto.message.WebtoonUpdateResultMessage;
import com.toonpick.service.WebtoonService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdatedWebtoonListener {
    private final WebtoonService webtoonService;
    private final ObjectMapper objectMapper;

    @Value("${spring.cloud.aws.sqs.queue.webtoon-update-complete}")
    private String queueName;

    @SqsListener(value = "${spring.cloud.aws.sqs.queue.webtoon-update-complete}")
    public void handle(String message) {
        try {
            log.info("데이터 픽업");
            WebtoonUpdateResultMessage wrapper = objectMapper.readValue(message, WebtoonUpdateResultMessage.class);
            webtoonService.updateWebtoon(wrapper.getUpdatedWebtoon());
        } catch (Exception e) {
            log.error("웹툰 업데이트 처리 실패. 메시지: {}", message, e);
        }
    }
}
