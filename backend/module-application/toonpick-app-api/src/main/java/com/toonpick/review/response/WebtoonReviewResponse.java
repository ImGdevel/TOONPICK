package com.toonpick.review.response;

import com.toonpick.dto.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebtoonReviewResponse {
    private Long id;
    private Long webtoonId;
    private MemberDTO member;
    private float rating;
    private String comment;
    private int likes;
    private LocalDateTime updatedDate;

    private Boolean isLiked;
}
