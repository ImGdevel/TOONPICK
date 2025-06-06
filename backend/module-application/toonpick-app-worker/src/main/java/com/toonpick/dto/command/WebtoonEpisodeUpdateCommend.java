package com.toonpick.dto.command;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebtoonEpisodeUpdateCommend {
    private Long webtoonId;

    private String platform;

    private String url;

    private String viewerType;

    private String title;

    private int episodeNumber;

    private String pricingType;
}
