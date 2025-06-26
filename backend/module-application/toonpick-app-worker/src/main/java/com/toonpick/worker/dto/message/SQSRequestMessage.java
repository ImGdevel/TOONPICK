package com.toonpick.worker.dto.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.worker.common.type.SQSEventType;
import com.toonpick.internal.aws.sqs.message.SQSMessage;
import com.toonpick.common.utils.UUIDUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SQSRequestMessage<T> implements SQSMessage {

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
