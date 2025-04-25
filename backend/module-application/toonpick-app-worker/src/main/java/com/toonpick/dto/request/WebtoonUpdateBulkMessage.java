package com.toonpick.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WebtoonUpdateBulkMessage {
    private int size;
    private List<WebtoonUpdatePayload> requests;
    private String requestTime;
}
