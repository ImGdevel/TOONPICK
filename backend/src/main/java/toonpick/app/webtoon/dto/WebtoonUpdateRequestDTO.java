package toonpick.app.webtoon.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toonpick.app.webtoon.entity.enums.AgeRating;
import toonpick.app.webtoon.entity.enums.SerializationStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebtoonUpdateRequestDTO {

    @NotNull
    private String platformId;

    private String thumbnailUrl;
    private String url;
    private SerializationStatus serializationStatus;
    private AgeRating ageRating;
    private String description;
    private Integer episodeCount;
    private LocalDate lastUpdatedDate;
    private DayOfWeek week;

    private Set<String> authors;
    private Set<String> genres;

    private Float platformRating;

}
