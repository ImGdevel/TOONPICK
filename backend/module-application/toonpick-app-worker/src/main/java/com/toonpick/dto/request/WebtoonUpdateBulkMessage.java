package com.toonpick.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.message.SQSMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebtoonUpdateBulkMessage implements SQSMessage {
    private int size;
    private List<WebtoonUpdatePayload> requests;
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
