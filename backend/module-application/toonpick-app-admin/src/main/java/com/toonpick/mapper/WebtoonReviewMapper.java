package com.toonpick.mapper;

import com.toonpick.dto.response.WebtoonReviewResponse;
import com.toonpick.domain.review.entity.WebtoonReview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WebtoonReviewMapper {
    WebtoonReviewResponse toWebtoonReviewResponse(WebtoonReview webtoonReview);
}
