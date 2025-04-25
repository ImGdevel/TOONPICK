package com.toonpick.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.message.SQSMessage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WebtoonCreateBulkMessage implements SQSMessage {
    private int size;
    private List<WebtoonCreatePayload> requests;
    private String requestTime;

    @Override
    public String toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

}
