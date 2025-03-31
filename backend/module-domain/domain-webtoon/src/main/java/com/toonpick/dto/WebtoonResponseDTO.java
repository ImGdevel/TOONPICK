package com.toonpick.dto;

import com.toonpick.enums.AgeRating;
import lombok.Builder;
import lombok.Data;
import com.toonpick.enums.Platform;
import com.toonpick.enums.SerializationStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
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
    private LocalDate lastUpdatedDate;
    private Float averageRating;
    private Float platformRating;
    private LocalDate publishStartDate;
    private LocalDate lastUpdateDate;
}
