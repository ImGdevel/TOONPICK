package com.toonpick.worker.listener.handler;

import com.toonpick.worker.common.type.SQSEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WebtoonEventHandlerFactory {
    
    private final List<WebtoonEventHandler> handlers;
    private Map<SQSEventType, WebtoonEventHandler> handlerMap;

    public WebtoonEventHandlerFactory(List<WebtoonEventHandler> handlers) {
        this.handlers = handlers;
    }

    @PostConstruct
    public void initialize() {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(
                        WebtoonEventHandler::getSupportedEventType,
                        Function.identity(),
                        (existing, replacement) -> {
                            return existing;
                        }
                ));
    }

    /**
     * 이벤트 타입에 맞는 핸들러를 반환
     * @param eventType 이벤트 타입
     * @return 해당 이벤트를 처리할 수 있는 핸들러
     */
    public WebtoonEventHandler getHandler(SQSEventType eventType) {
        WebtoonEventHandler handler = handlerMap.get(eventType);
        if (handler == null) {
            throw new IllegalArgumentException("지원하지 않는 이벤트 타입: " + eventType);
        }
        return handler;
    }

    /**
     * 특정 이벤트 타입을 처리할 수 있는 핸들러가 있는지 확인
     * @param eventType 이벤트 타입
     * @return 처리 가능 여부
     */
    public boolean supports(SQSEventType eventType) {
        return handlerMap.containsKey(eventType);
    }
} 