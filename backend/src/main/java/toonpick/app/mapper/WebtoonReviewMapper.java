package toonpick.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import toonpick.app.dto.WebtoonReviewCreateDTO;
import toonpick.app.dto.WebtoonReviewDTO;
import toonpick.app.entity.WebtoonReview;

@Mapper
public interface WebtoonReviewMapper {
    WebtoonReviewMapper INSTANCE = Mappers.getMapper(WebtoonReviewMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "webtoon.id", target = "webtoonId")
    WebtoonReviewDTO toDTO(WebtoonReview review);

    // user와 webtoon을 실제로 설정하기 위한 추가 매핑
    WebtoonReview toEntity(WebtoonReviewCreateDTO reviewCreateDTO);
}
