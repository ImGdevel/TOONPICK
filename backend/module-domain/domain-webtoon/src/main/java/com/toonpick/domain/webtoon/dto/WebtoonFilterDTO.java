package com.toonpick.domain.webtoon.dto;

import com.toonpick.domain.webtoon.enums.AgeRating;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebtoonFilterDTO {
    private Set<String> platforms;
    private Set<SerializationStatus> serializationStatuses;
    private Set<AgeRating> ageRatings;
    private Set<DayOfWeek> publishDays;
    private Set<String> genres;
    private Set<String> authors;
}
