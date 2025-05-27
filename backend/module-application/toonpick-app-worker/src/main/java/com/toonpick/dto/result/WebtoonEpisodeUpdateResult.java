package com.toonpick.dto.result;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebtoonEpisodeUpdateResult {
    private Long id;

    private String platform;

    private String url;
}
