package toonpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toonpick.app.entity.enums.AgeRating;
import toonpick.app.entity.enums.Platform;
import toonpick.app.entity.enums.SerializationStatus;

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
