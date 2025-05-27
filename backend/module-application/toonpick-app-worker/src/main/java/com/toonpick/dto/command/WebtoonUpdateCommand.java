package com.toonpick.dto.command;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebtoonUpdateCommand {
    private Long id;

    private String platform;

    private String url;

    private int episodeCount;

}
