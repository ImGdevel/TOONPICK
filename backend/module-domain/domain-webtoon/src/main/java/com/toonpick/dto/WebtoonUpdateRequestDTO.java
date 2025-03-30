package com.toonpick.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebtoonUpdateRequestDTO {
    private Long id;
    private String title;
    private String platform;
    private String link;
}
