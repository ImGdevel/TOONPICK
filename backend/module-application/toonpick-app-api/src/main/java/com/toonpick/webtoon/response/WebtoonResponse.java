package com.toonpick.webtoon.response;

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
    private SerializationStatus serializationStatus;
    private DayOfWeek dayOfWeek;
    private String thumbnailUrl;
    private String link;
    private AgeRating ageRating;
    private Set<AuthorDTO> authors;
    private Set<GenreDTO> genres;
    private int episodeCount;
    private Float averageRating;
    private LocalDate publishStartDate;
    private LocalDate lastUpdateDate;
}
