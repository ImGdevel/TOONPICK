package com.toonpick.review.mapper;

import com.toonpick.dto.WebtoonReviewCreateDTO;
import com.toonpick.entity.WebtoonReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.toonpick.dto.WebtoonReviewDTO;


@Mapper
public interface WebtoonReviewMapper {
    WebtoonReviewMapper INSTANCE = Mappers.getMapper(WebtoonReviewMapper.class);


    WebtoonReviewDTO webtoonReviewToWebtoonReviewDTO(WebtoonReview review);

    @Mapping(target = "member", ignore = true)
    WebtoonReview webtoonReviewDTOToWebtoonReview(WebtoonReviewCreateDTO reviewCreateDTO);
}
