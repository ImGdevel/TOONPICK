package com.toonpick.dto.command;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebtoonEpisodeUpdateCommand {

    private Long webtoonId;
    private String platform;
    private List<EpisodeInfo> episodes;
    private String lastUpdatedDate;
    private String status;
}