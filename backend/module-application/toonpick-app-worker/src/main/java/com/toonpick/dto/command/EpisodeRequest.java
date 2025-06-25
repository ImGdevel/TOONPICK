package com.toonpick.dto.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EpisodeRequest {

    @JsonProperty("title")
    private String title;

    @JsonProperty("uploadDate")
    private String uploadDate;

    @JsonProperty("url")
    private String webUrl;

    @JsonProperty("episodeNumber")
    @NotNull(message = "episode에 number는 null일 수 없습니다.")
    private Integer episodeNumber;

    @JsonProperty("thumbnailUrl")
    private String thumbnailUrl;

    @JsonProperty("pricingType")
    private String pricingType;

    @JsonProperty("daysUntilFree")
    private Integer daysUntilFree;

    @JsonProperty("mobileUrl")
    private String mobileUrl;
}