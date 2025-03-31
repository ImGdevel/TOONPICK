package com.toonpick.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebtoonReviewCreateDTO {
    private Long webtoonId;
    private float rating;
    private String comment;
}
