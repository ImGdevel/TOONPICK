package com.toonpick.worker.infrastructure.messaging.handler;

import com.toonpick.worker.common.type.SQSEventType;

/**
 * 웹툰 이벤트 처리 전략 인터페이스
 */
public interface WebtoonEventHandler {
    
    /**
     * 이 핸들러가 처리할 수 있는 이벤트 타입을 반환
     */
    SQSEventType getSupportedEventType();
    
    /**
     * 메시지를 처리
     * @param message 원본 메시지
     */
    void handle(String message);
} 