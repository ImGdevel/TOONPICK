package com.toonpick.dto.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebtoonEpisodeCrawItem {

    private Long id;
    private String platform;
    private String url;
    private int episodeCount;

}

