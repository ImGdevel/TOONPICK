package toonpick.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toonpick.enums.AgeRating;
import toonpick.enums.Platform;
import toonpick.enums.SerializationStatus;

import java.time.DayOfWeek;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebtoonFilterDTO {
    private Platform platform;
    private SerializationStatus serializationStatus;
    private AgeRating ageRating;
    private DayOfWeek week;
    private Set<String> genres;
    private Set<String> authors;
}
