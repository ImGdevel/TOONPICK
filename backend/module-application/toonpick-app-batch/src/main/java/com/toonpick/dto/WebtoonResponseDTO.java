package com.toonpick.dto;

import com.toonpick.enums.AgeRating;
import com.toonpick.enums.Platform;
import com.toonpick.enums.SerializationStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Data
@SuperBuilder
public class WebtoonResponseDTO {
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
