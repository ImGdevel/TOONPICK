package com.toonpick.mapper;

import com.toonpick.dto.response.WebtoonReviewResponse;
import com.toonpick.entity.WebtoonReview;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WebtoonReviewMapper {
    WebtoonReviewMapper INSTANCE = Mappers.getMapper(WebtoonReviewMapper.class);

    WebtoonReviewResponse toWebtoonReviewResponse(WebtoonReview webtoonReview);
}
