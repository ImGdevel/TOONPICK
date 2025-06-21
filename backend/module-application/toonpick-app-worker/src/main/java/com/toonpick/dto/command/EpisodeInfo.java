package com.toonpick.dto.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EpisodeInfo {

    private Long episodeId;
    private String title;
    private String thumbnailUrl;
    private String uploadDate;
    private int episodeNumber;

    @JsonProperty("link")
    private String webUrl;

    private String mobileUrl;

    private String pricingType;
}