package com.toonpick.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.common.type.SQSEventType;
import com.toonpick.message.SQSMessage;
import com.toonpick.common.utils.UUIDUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SQSResponseMessage<T> implements SQSMessage {

    @Builder.Default
    private String requestId = UUIDUtils.generateUUID();
    private SQSEventType eventType;
    private T data;
    private String message;
    private long requestTime = System.currentTimeMillis();

    @Override
    public String toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            return "{}";
        }
    }
}