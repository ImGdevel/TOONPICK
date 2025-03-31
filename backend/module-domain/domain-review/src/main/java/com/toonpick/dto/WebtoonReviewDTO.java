package com.toonpick.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebtoonReviewDTO {
    private Long id;
    private Long webtoonId;
    private MemberProfileResponseDTO memberId;
    private float rating;
    private String comment;
    private int likes;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
