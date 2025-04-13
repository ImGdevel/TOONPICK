package com.toonpick.review.mapper;

import com.toonpick.entity.WebtoonReview;
import com.toonpick.review.request.WebtoonReviewCreateDTO;
import com.toonpick.review.response.WebtoonReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface WebtoonReviewMapper {
    WebtoonReviewMapper INSTANCE = Mappers.getMapper(WebtoonReviewMapper.class);


    WebtoonReviewDTO webtoonReviewToWebtoonReviewDTO(WebtoonReview review);

    @Mapping(target = "member", ignore = true)
    WebtoonReview webtoonReviewDTOToWebtoonReview(WebtoonReviewCreateDTO reviewCreateDTO);
}
