package com.toonpick.app.mapper;

import com.toonpick.app.dto.ReviewDTO;
import com.toonpick.app.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class, WebtoonMapper.class})
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "webtoon", target = "webtoon")
    ReviewDTO reviewToReviewDto(Review review);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "webtoon", target = "webtoon")
    Review reviewDtoToReview(ReviewDTO reviewDTO);
}