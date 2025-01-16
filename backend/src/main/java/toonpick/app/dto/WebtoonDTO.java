package toonpick.app.dto;

import lombok.*;
import toonpick.app.domain.webtoon.enums.AgeRating;
import toonpick.app.domain.webtoon.enums.Platform;
import toonpick.app.domain.webtoon.enums.SerializationStatus;

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
    private String platformId;
    private Platform platform;
    private float averageRating;
    private float platformRating;
    private String description;
    private int episodeCount;
    private LocalDate serializationStartDate;
    private LocalDate lastUpdatedDate;
    private SerializationStatus serializationStatus;
    private DayOfWeek week;
    private Set<AuthorDTO> authors;
    private Set<GenreDTO> genres;
    private String thumbnailUrl;
    private String url;
    private AgeRating ageRating;
}
