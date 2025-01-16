package toonpick.app.mapper;

import toonpick.app.dto.WebtoonDTO;
import toonpick.app.domain.webtoon.Webtoon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import toonpick.app.dto.webtoon.WebtoonResponseDTO;

@Mapper(componentModel = "spring")
public interface WebtoonMapper {
    WebtoonMapper INSTANCE = Mappers.getMapper(WebtoonMapper.class);

    @Mapping(source = "authors", target = "authors")
    @Mapping(source = "genres", target = "genres")
    WebtoonResponseDTO webtoonToWebtoonResponseDto(Webtoon webtoon);
}
