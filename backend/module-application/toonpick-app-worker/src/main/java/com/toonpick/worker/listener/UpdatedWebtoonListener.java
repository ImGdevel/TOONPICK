package com.toonpick.worker.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.worker.common.type.SQSEventType;
import com.toonpick.worker.listener.handler.WebtoonEventHandlerFactory;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdatedWebtoonListener {
    
    private final WebtoonEventHandlerFactory handlerFactory;
    private final ObjectMapper objectMapper;

    @SqsListener(value = "${spring.cloud.aws.sqs.queue.webtoon-update-complete}")
    public void handle(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            SQSEventType eventType = SQSEventType.valueOf(root.get("eventType").asText());

            if (!handlerFactory.supports(eventType)) {
                log.warn("지원하지 않는 이벤트 타입: {}", eventType);
                return;
            }

            handlerFactory.getHandler(eventType).handle(message);
            
        } catch (Exception e) {
            log.error("웹툰 업데이트 처리 실패. 메시지: {} // ", message, e);
        }
    }
}
