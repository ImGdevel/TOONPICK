package com.toonpick.review.mapper;

import com.toonpick.entity.WebtoonReview;
import com.toonpick.review.response.WebtoonReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface WebtoonReviewMapper {
    WebtoonReviewMapper INSTANCE = Mappers.getMapper(WebtoonReviewMapper.class);

    WebtoonReviewResponse toWebtoonReviewResponse(WebtoonReview review);

}
