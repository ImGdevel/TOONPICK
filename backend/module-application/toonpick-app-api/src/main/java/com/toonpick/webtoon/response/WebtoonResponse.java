package com.toonpick.webtoon.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.toonpick.dto.AuthorDTO;
import com.toonpick.dto.GenreDTO;
import com.toonpick.enums.AgeRating;
import lombok.Data;
import com.toonpick.enums.Platform;
import com.toonpick.enums.SerializationStatus;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Data
@SuperBuilder
public class WebtoonResponse {

    private Long id;

    private String title;

    private Platform platform;

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

    @JsonProperty("totalRatings")
    private int episodeCount;

    private Float averageRating;

    private LocalDate publishStartDate;

    private LocalDate lastUpdateDate;
}
