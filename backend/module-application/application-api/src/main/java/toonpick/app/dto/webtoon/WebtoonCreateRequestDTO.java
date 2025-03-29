package toonpick.app.dto.webtoon;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import toonpick.app.domain.webtoon.enums.AgeRating;
import toonpick.app.domain.webtoon.enums.Platform;
import toonpick.app.domain.webtoon.enums.SerializationStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class WebtoonCreateRequestDTO {

    @JsonProperty("title")
    private String title;

    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("platform")
    private Platform platform;

    @JsonProperty("day_of_week")
    private DayOfWeek dayOfWeek;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("link")
    private String link;

    @JsonProperty("age_rating")
    private AgeRating ageRating;

    @JsonProperty("description")
    private String description;

    @JsonProperty("serialization_status")
    private SerializationStatus serializationStatus;

    @JsonProperty("episode_count")
    private int episodeCount;

    @JsonProperty("platform_rating")
    private float platformRating;

    @JsonProperty("publish_start_date")
    private LocalDate publishStartDate;

    @JsonProperty("last_updated_date")
    private LocalDate lastUpdatedDate;

    @JsonProperty("authors")
    private Set<AuthorDTO> authors;

    @JsonProperty("genres")
    private Set<String> genres;
}
