package com.toonpick.dto.command;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("webtoonId")
    private String webtoonId;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("platform")
    private String platform;
    
    @JsonProperty("episodes")
    private List<EpisodeInfo> episodes;
    
    @JsonProperty("lastUpdatedDate")
    private String lastUpdatedDate;
    
    @JsonProperty("status")
    private String status;
}