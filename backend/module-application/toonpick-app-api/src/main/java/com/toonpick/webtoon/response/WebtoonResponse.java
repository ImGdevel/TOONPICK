package com.toonpick.webtoon.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.toonpick.domain.webtoon.enums.AgeRating;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Getter
@SuperBuilder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebtoonResponse {

    private Long id;

    private String title;

    private String description;

    @JsonProperty("status")
    private SerializationStatus serializationStatus;

    @JsonProperty("publishDay")
    private DayOfWeek dayOfWeek;

    private String thumbnailUrl;

    private String link;

    @JsonProperty("isAdult")
    private boolean isAdult;

    private AgeRating ageRating;

    private Set<AuthorResponse> authors;

    private Set<GenreResponse> genres;

    private Set<PlatformResponse> platforms;

    @JsonProperty("totalRatings")
    private int episodeCount;

    private Float averageRating;

    private LocalDate publishStartDate;

    private LocalDate lastUpdateDate;
}
