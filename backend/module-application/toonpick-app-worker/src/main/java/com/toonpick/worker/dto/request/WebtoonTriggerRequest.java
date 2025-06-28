package com.toonpick.worker.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 웹툰 트리거 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebtoonTriggerRequest {
    private String id;
    private String platform;
    private String webtoon_url;
} 