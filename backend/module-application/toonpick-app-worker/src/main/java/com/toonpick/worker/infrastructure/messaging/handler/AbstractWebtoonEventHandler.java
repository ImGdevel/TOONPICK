package com.toonpick.worker.infrastructure.messaging.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 웹툰 이벤트 핸들러의 공통 기능을 제공하는 추상 클래스
 */
@Slf4j
public abstract class AbstractWebtoonEventHandler implements WebtoonEventHandler {
    
    protected final ObjectMapper objectMapper;
    protected final Validator validator;

    protected AbstractWebtoonEventHandler(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @Override
    public void handle(String message) {
        try {
            Object command = extractCommand(message);
            
            if (command == null) {
                log.error("역직렬화된 Command가 null입니다. 원본 메시지: {}", message);
                return;
            }

            if (!validateCommand(command)) {
                return;
            }

            processCommand(command);
            
        } catch (Exception e) {
            log.error("이벤트 처리 실패. 이벤트 타입: {}, 메시지: {}", getSupportedEventType(), message, e);
        }
    }

    /**
     * 메시지에서 Command 객체를 추출
     */
    protected Object extractCommand(String message) throws Exception {
        JsonNode root = objectMapper.readTree(message);
        JsonNode dataNode = root.get("data");
        
        if (dataNode == null) {
            log.error("data 필드가 없습니다. 원본 메시지: {}", message);
            throw new IllegalArgumentException("data 필드가 없습니다.");
        }

        return objectMapper.treeToValue(dataNode, getCommandClass());
    }

    /**
     * Command 객체의 유효성 검사
     */
    protected boolean validateCommand(Object command) {
        Set<ConstraintViolation<Object>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Object> violation : violations) {
                log.error("Command 유효성 실패 - {}: {}", 
                    violation.getPropertyPath(), violation.getMessage());
            }
            return false;
        }
        return true;
    }

    /**
     * Command 객체를 처리하는 추상 메서드
     */
    protected abstract void processCommand(Object command);

    /**
     * Command 클래스를 반환하는 추상 메서드
     */
    protected abstract Class<?> getCommandClass();
} 