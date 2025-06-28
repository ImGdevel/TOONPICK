package com.toonpick.worker.dto.command;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebtoonEpisodeUpdateCommand {

    @JsonProperty("webtoonId")
    @NotNull(message = "EpisodeUpdate에 webtoonId는 null일 수 없습니다.")
    private Long webtoonId;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("platform")
    private String platform;
    
    @JsonProperty("episodes")
    private List<EpisodeRequest> episodes;
    
    @JsonProperty("lastUpdatedDate")
    private String lastUpdatedDate;
    
    @JsonProperty("status")
    private String status;
}