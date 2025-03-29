package toonpick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import toonpick.dto.WebtoonReviewCreateDTO;
import toonpick.dto.WebtoonReviewDTO;
import toonpick.entity.WebtoonReview;


@Mapper
public interface WebtoonReviewMapper {
    WebtoonReviewMapper INSTANCE = Mappers.getMapper(WebtoonReviewMapper.class);


    WebtoonReviewDTO webtoonReviewToWebtoonReviewDTO(WebtoonReview review);

    @Mapping(target = "member", ignore = true)
    WebtoonReview webtoonReviewDTOToWebtoonReview(WebtoonReviewCreateDTO reviewCreateDTO);
}
