package com.toonpick.dto.command;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    private String episodeId;
    private String title;
    private String thumbnailUrl;
    private String uploadDate;
    private int episodeNumber;
    private String url;
    private String pricingType;
    private String viewerType;

}