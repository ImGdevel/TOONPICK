package com.toonpick.webtoon.request;

import com.toonpick.dto.WebtoonFilterDTO;
import com.toonpick.enums.AgeRating;
import lombok.Builder;
import lombok.Data;
import com.toonpick.enums.SerializationStatus;

import java.time.DayOfWeek;
import java.util.Set;

@Data
@Builder
public class WebtoonFilterRequest {
    private Set<String> platforms;
    private Set<SerializationStatus> serializationStatuses;
    private Set<AgeRating> ageRatings;
    private Set<DayOfWeek> publishDays;
    private Set<String> genres;
    private Set<String> authors;

    public WebtoonFilterDTO toFilterDTO(){
        return WebtoonFilterDTO.builder()
                .platforms(platforms)
                .serializationStatuses(serializationStatuses)
                .ageRatings(ageRatings)
                .publishDays(publishDays)
                .genres(genres)
                .authors(authors)
                .build();
    }
}
