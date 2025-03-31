package com.toonpick.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.message.SQSMessage;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

@Data
@Builder
public class WebtoonUpdateBatchRequestDTO implements SQSMessage {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String requestId;
    private int totalCount;
    private List<WebtoonUpdateRequestDTO> webtoons;

    @Override
    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert WebtoonUpdateBatchRequestDTO to JSON: " + e.getMessage(), e);
        }
    }
}
