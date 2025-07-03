package com.toonpick.review.mapper;

import com.toonpick.domain.review.entity.WebtoonReview;
import com.toonpick.review.response.WebtoonReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface WebtoonReviewMapper {
    WebtoonReviewMapper INSTANCE = Mappers.getMapper(WebtoonReviewMapper.class);

    WebtoonReviewResponse toWebtoonReviewResponse(WebtoonReview review);

}
