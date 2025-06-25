package com.toonpick.listener;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.common.type.SQSEventType;
import com.toonpick.dto.command.WebtoonCreateCommend;
import com.toonpick.dto.command.WebtoonEpisodeUpdateCommand;
import com.toonpick.service.WebtoonEpisodeUpdateService;
import com.toonpick.service.WebtoonRegistrationService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;


@Slf4j
@RequiredArgsConstructor
@Component
public class UpdatedWebtoonListener {
    private final WebtoonRegistrationService webtoonRegistrationService;
    private final WebtoonEpisodeUpdateService webtoonEpisodeUpdateService;
    private final ObjectMapper objectMapper;

    private final Validator validator;

    @Value("${spring.cloud.aws.sqs.queue.webtoon-update-complete}")
    private String queueName;

    @SqsListener(value = "${spring.cloud.aws.sqs.queue.webtoon-update-complete}")
    public void handle(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            SQSEventType eventType = SQSEventType.valueOf(root.get("eventType").asText());

            log.info("웹툰 업데이트 메시지 수신 : {} | {}", eventType, message);

            switch (eventType) {
                case CRAWL_WEBTOON_EPISODE:
                    handleEpisodeUpdate(message);
                    break;
                case CRAWL_WEBTOON_ALL:
                case CRAWL_WEBTOON_NEW:
                    handleWebtoonCreate(message);
                    break;
            }

        } catch (Exception e) {
            log.error("웹툰 업데이트 처리 실패. 메시지: {}", message, e);
        }
    }

    private void handleEpisodeUpdate(String message) throws Exception {
        WebtoonEpisodeUpdateCommand command = extractEpisodeUpdateCommand(message);

        if (command.getWebtoonId() == null || command.getEpisodes() == null) {
            log.error("역직렬화된 WebtoonEpisodeUpdateCommand에 필수 필드 누락. 원본 메시지: {}", message);
            return;
        }

        Set<ConstraintViolation<WebtoonEpisodeUpdateCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<WebtoonEpisodeUpdateCommand> violation : violations) {
                log.error("WebtoonEpisodeUpdateCommand 유효성 실패 - {}: {}", violation.getPropertyPath(), violation.getMessage());
            }
            return;
        }

        webtoonEpisodeUpdateService.registerEpisodes(command);
    }

    private void handleWebtoonCreate(String message) throws Exception {
        WebtoonCreateCommend command = extractWebtoonCreateCommand(message);

        if (command == null) {
            log.error("역직렬화된 WebtoonCreateCommend가 null입니다. 원본 메시지: {}", message);
            return;
        }

        Set<ConstraintViolation<WebtoonCreateCommend>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<WebtoonCreateCommend> violation : violations) {
                log.error("WebtoonCreateCommend 유효성 실패 - {}: {}", violation.getPropertyPath(), violation.getMessage());
            }
            return;
        }

        webtoonRegistrationService.createWebtoon(command);
    }

    private WebtoonEpisodeUpdateCommand extractEpisodeUpdateCommand(String message) throws Exception {
        JsonNode root = objectMapper.readTree(message);
        JsonNode dataNode = root.get("data");
        
        if (dataNode == null) {
            log.error("data 필드가 없습니다. 원본 메시지: {}", message);
            throw new IllegalArgumentException("data 필드가 없습니다.");
        }

        return objectMapper.treeToValue(dataNode, WebtoonEpisodeUpdateCommand.class);
    }

    private WebtoonCreateCommend extractWebtoonCreateCommand(String message) throws Exception {
        JsonNode root = objectMapper.readTree(message);
        JsonNode dataNode = root.get("data");
        
        if (dataNode == null) {
            log.error("data 필드가 없습니다. 원본 메시지: {}", message);
            throw new IllegalArgumentException("data 필드가 없습니다.");
        }

        return objectMapper.treeToValue(dataNode, WebtoonCreateCommend.class);
    }
}
