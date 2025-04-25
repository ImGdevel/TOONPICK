package com.toonpick.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WebtoonCreateBulkMessage {
    private int size;
    private List<WebtoonCreatePayload> requests;
    private String requestTime;

}
