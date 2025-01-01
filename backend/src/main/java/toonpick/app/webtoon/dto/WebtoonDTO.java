package toonpick.app.webtoon.dto;

import lombok.*;
import toonpick.app.webtoon.entity.enums.AgeRating;
import toonpick.app.webtoon.entity.enums.Platform;
import toonpick.app.webtoon.entity.enums.SerializationStatus;

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
