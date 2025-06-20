package com.toonpick.listener;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.common.type.SQSEventType;
import com.toonpick.dto.command.WebtoonCreateCommend;
import com.toonpick.dto.command.WebtoonEpisodeUpdateCommand;
import com.toonpick.dto.message.SQSResponseMessage;
import com.toonpick.service.WebtoonEpisodeUpdateService;
import com.toonpick.service.WebtoonRegistrationService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdatedWebtoonListener {
    private final WebtoonRegistrationService webtoonRegistrationService;
    private final WebtoonEpisodeUpdateService webtoonEpisodeUpdateService;
    private final ObjectMapper objectMapper;

    @Value("${spring.cloud.aws.sqs.queue.webtoon-update-complete}")
    private String queueName;

    @SqsListener(value = "${spring.cloud.aws.sqs.queue.webtoon-update-complete}")
    public void handle(String message) {
        try {

            // 먼저 eventType 파싱
            JsonNode root = objectMapper.readTree(message);
            SQSEventType eventType = SQSEventType.valueOf(root.get("eventType").asText());

            log.info("웹툰 업데이트 메시지 수신 : {} | {}", eventType, message);

            // 이벤트 타입에 따라 파싱
            switch (eventType) {
                case CRAWL_WEBTOON_EPISODE: {
                    SQSResponseMessage<WebtoonEpisodeUpdateCommand> responseMessage =
                            objectMapper.readValue(message, new TypeReference<SQSResponseMessage<WebtoonEpisodeUpdateCommand>>() {});
                    webtoonEpisodeUpdateService.registerEpisodes(responseMessage.getData());
                    break;
                }
                case CRAWL_WEBTOON_ALL:

                    break;

                case CRAWL_WEBTOON_NEW:
                    SQSResponseMessage<WebtoonCreateCommend> responseMessage =
                            objectMapper.readValue(message, new TypeReference<SQSResponseMessage<WebtoonCreateCommend>>() {});
                    webtoonRegistrationService.createWebtoon(responseMessage.getData());
                    break;
            }

        } catch (Exception e) {
            log.error("웹툰 업데이트 처리 실패. 메시지: {}", message, e);
        }
    }
}
