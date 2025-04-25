package com.toonpick.review.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebtoonReviewUpdateRequest {

    @NotNull(message = "Review ID는 필수입니다")
    Long reviewId;

    @NotNull(message = "평점은 필수입니다")
    @Min(value = 1, message = "평점은 최소 1점 이상이어야 합니다")
    @Max(value = 5, message = "평점은 최대 5점 이하여야 합니다")
    float rating;

    @Size(max = 1000, message = "리뷰는 1000자 이내로 작성해주세요")
    String comment;
}
