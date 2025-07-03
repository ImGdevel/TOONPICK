package com.toonpick.worker.infrastructure.messaging.handler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.worker.common.type.SQSEventType;
import com.toonpick.worker.domain.service.WebtoonRegistrationService;
import com.toonpick.worker.dto.command.WebtoonCreateCommend;
import com.toonpick.worker.infrastructure.messaging.handler.AbstractWebtoonEventHandler;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebtoonCreateHandler extends AbstractWebtoonEventHandler {
    
    private final WebtoonRegistrationService webtoonRegistrationService;

    public WebtoonCreateHandler(ObjectMapper objectMapper, 
                               Validator validator,
                               WebtoonRegistrationService webtoonRegistrationService) {
        super(objectMapper, validator);
        this.webtoonRegistrationService = webtoonRegistrationService;
    }

    @Override
    public SQSEventType getSupportedEventType() {
        return SQSEventType.CRAWL_WEBTOON_ALL;
    }

    @Override
    protected void processCommand(Object command) {
        WebtoonCreateCommend createCommand = (WebtoonCreateCommend) command;
        webtoonRegistrationService.createWebtoon(createCommand);
        log.info("웹툰 생성/업데이트 처리 완료 - 제목: {}, 플랫폼: {}", createCommand.getTitle(), createCommand.getPlatform());
    }

    @Override
    protected Class<?> getCommandClass() {
        return WebtoonCreateCommend.class;
    }
} 