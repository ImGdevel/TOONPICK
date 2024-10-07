package toonpick.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import toonpick.app.dto.WebtoonReviewDTO;
import toonpick.app.entity.WebtoonReview;

@Mapper(componentModel = "spring")
public interface WebtoonReviewMapper {
    WebtoonReviewMapper INSTANCE = Mappers.getMapper(WebtoonReviewMapper.class);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "webtoon", target = "webtoon")
    WebtoonReviewDTO toDTO(WebtoonReview userRating);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "webtoon", target = "webtoon")
    WebtoonReview toEntity(WebtoonReviewDTO webtoonReviewDTO);
}
