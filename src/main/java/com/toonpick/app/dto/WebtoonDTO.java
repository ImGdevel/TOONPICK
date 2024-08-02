package com.toonpick.app.dto;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebtoonDTO {
    private Long id;
    private String title;
    private float averageRating;
    private float platformRating;
    private String description;
    private int episodeCount;
    private LocalDate serializationStartDate;
    private DayOfWeek serializationDay;
    private Set<AuthorDTO> authors;
    private Set<GenreDTO> genres;
}