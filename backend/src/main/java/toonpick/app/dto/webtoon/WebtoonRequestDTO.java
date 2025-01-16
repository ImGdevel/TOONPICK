package toonpick.app.dto.webtoon;

import lombok.Data;
import toonpick.app.domain.webtoon.enums.AgeRating;
import toonpick.app.domain.webtoon.enums.Platform;
import toonpick.app.domain.webtoon.enums.SerializationStatus;

import java.time.DayOfWeek;
import java.util.Set;

@Data
public class WebtoonRequestDTO {
    private String title;
    private Platform platform;
    private String description;
    private SerializationStatus serializationStatus;
    private DayOfWeek week;
    private String thumbnailUrl;
    private String url;
    private AgeRating ageRating;
    private Set<Long> authorIds;
    private Set<Long> genreIds;
}
