package com.toonpick.dto;

import com.toonpick.enums.AgeRating;
import com.toonpick.enums.Platform;
import com.toonpick.enums.SerializationStatus;
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
    private Platform platform;
    private SerializationStatus serializationStatus;
    private AgeRating ageRating;
    private DayOfWeek week;
    private Set<String> genres;
    private Set<String> authors;
}
