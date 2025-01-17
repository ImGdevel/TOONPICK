package toonpick.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import toonpick.app.dto.WebtoonReviewCreateDTO;
import toonpick.app.dto.WebtoonReviewDTO;
import toonpick.app.domain.review.WebtoonReview;

@Mapper
public interface WebtoonReviewMapper {
    WebtoonReviewMapper INSTANCE = Mappers.getMapper(WebtoonReviewMapper.class);


    WebtoonReviewDTO toDTO(WebtoonReview review);

    @Mapping(target = "member", ignore = true)
    WebtoonReview toEntity(WebtoonReviewCreateDTO reviewCreateDTO);
}
