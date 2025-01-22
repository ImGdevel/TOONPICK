package toonpick.app.dto.webtoon;

import lombok.Builder;
import lombok.Data;
import toonpick.app.domain.webtoon.enums.AgeRating;
import toonpick.app.domain.webtoon.enums.Platform;
import toonpick.app.domain.webtoon.enums.SerializationStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class WebtoonCreateRequestDTO {

    private String title;
    private String externalId;
    private Platform platform;
    private DayOfWeek dayOfWeek;
    private String thumbnailUrl;
    private String link;
    private AgeRating ageRating;
    private String description;
    private SerializationStatus serializationStatus;
    private int episodeCount;
    private float platformRating;
    private LocalDate publishStartDate;
    private LocalDate lastUpdatedDate;
    private Set<AuthorDTO> authors;
    private Set<GenreDTO> genres;
}
