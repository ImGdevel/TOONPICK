package com.toonpick.worker.dto.command;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.toonpick.domain.webtoon.enums.AgeRating;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebtoonCreateCommend {

    @JsonProperty("id")
    private String titleId;

    @JsonProperty("url")
    @NotNull(message = "webtoon에 url는 null일 수 없습니다.")
    private String url;

    @JsonProperty("title")
    @NotNull(message = "webtoon에 title은 null일 수 없습니다.")
    private String title;

    @JsonProperty("uniqueId")
    private String uniqueId;

    @JsonProperty("platform")
    @NotNull(message = "webtoon에 platform은 null일 수 없습니다.")
    private String platform;

    @JsonProperty("description")
    private String description;

    @JsonProperty("thumbnailUrl")
    private String thumbnailUrl;

    @JsonProperty("dayOfWeek")
    @NotNull(message = "webtoon에 dayOfWeek는 null일 수 없습니다.")
    private String dayOfWeek;

    @JsonProperty("status")
    @NotNull(message = "webtoon에 status는 null일 수 없습니다.")
    private SerializationStatus status;

    @JsonProperty("ageRating")
    private AgeRating ageRating;

    @JsonProperty("episodeCount")
    private Integer episodeCount;

    @JsonProperty("previewCount")
    private Integer previewCount;

    @JsonProperty("genres")
    private List<String> genres;

    @JsonProperty("authors")
    private List<AuthorRequest> authors;

    @JsonProperty("publishStartDate")
    private String publishStartDate;

    @JsonProperty("lastUpdatedDate")
    private String lastUpdatedDate;

    @JsonProperty("episodes")
    private List<EpisodeRequest> episodes;

    @JsonProperty("relatedNovels")
    private List<RelatedNovelRequest> relatedNovels;

    @JsonProperty("relatedWebtoonIds")
    private List<String> relatedWebtoonIds;
}