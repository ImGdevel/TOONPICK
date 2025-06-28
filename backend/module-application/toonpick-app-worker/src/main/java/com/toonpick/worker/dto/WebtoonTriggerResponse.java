package com.toonpick.worker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebtoonTriggerResponse {
    private String message;
    private int processedCount;
    private String status;
    private long timestamp;
} 