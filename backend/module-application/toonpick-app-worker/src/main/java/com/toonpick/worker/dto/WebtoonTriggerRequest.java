package com.toonpick.worker.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebtoonTriggerRequest {
    private String id;
    private String webtoon_url;
    private String platform;
} 