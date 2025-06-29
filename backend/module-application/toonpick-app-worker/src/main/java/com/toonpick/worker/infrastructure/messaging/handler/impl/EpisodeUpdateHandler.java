package com.toonpick.worker.infrastructure.messaging.handler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.worker.common.type.SQSEventType;
import com.toonpick.worker.domain.service.WebtoonEpisodeUpdateService;
import com.toonpick.worker.dto.command.WebtoonEpisodeUpdateCommand;
import com.toonpick.worker.infrastructure.messaging.handler.AbstractWebtoonEventHandler;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EpisodeUpdateHandler extends AbstractWebtoonEventHandler {
    
    private final WebtoonEpisodeUpdateService webtoonEpisodeUpdateService;

    public EpisodeUpdateHandler(ObjectMapper objectMapper, 
                               Validator validator,
                               WebtoonEpisodeUpdateService webtoonEpisodeUpdateService) {
        super(objectMapper, validator);
        this.webtoonEpisodeUpdateService = webtoonEpisodeUpdateService;
    }

    @Override
    public SQSEventType getSupportedEventType() {
        return SQSEventType.CRAWL_WEBTOON_EPISODE;
    }

    @Override
    protected void processCommand(Object command) {
        WebtoonEpisodeUpdateCommand episodeCommand = (WebtoonEpisodeUpdateCommand) command;
        webtoonEpisodeUpdateService.registerEpisodes(episodeCommand);
        log.info("웹툰 에피소드 업데이트 처리 완료 - 제목: {}, 플랫폼: {}", episodeCommand.getTitle(), episodeCommand.getPlatform());
    }

    @Override
    protected Class<?> getCommandClass() {
        return WebtoonEpisodeUpdateCommand.class;
    }
} 