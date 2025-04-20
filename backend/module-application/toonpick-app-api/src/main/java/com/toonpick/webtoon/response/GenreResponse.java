package com.toonpick.webtoon.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreResponse {
    private Long id;
    private String name;
    private String type;
    private boolean display;
}
