package toonpick.dto;

import lombok.Builder;
import lombok.Data;
import toonpick.enums.AgeRating;
import toonpick.enums.Platform;
import toonpick.enums.SerializationStatus;

import java.time.DayOfWeek;
import java.util.Set;

@Data
@Builder
public class WebtoonRequestDTO {
    private String title;
    private Platform platform;
    private String description;
    private SerializationStatus serializationStatus;
    private DayOfWeek dayOfWeek;
    private String thumbnailUrl;
    private String link;
    private AgeRating ageRating;
    private Set<Long> authorIds;
    private Set<Long> genreIds;
}
