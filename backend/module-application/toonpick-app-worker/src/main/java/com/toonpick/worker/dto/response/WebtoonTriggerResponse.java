package com.toonpick.worker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 웹툰 트리거 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebtoonTriggerResponse {
    private boolean success;
    private String message;
    private String taskId;
    private int processedCount;
    private long timestamp;
    
    public WebtoonTriggerResponse(boolean success, String message, String taskId, int processedCount) {
        this.success = success;
        this.message = message;
        this.taskId = taskId;
        this.processedCount = processedCount;
        this.timestamp = System.currentTimeMillis();
    }
} 