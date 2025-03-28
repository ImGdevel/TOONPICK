package toonpick.app.mapper;

import toonpick.app.domain.webtoon.Webtoon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import toonpick.app.dto.webtoon.WebtoonResponseDTO;
import toonpick.app.dto.webtoon.WebtoonUpdateRequestDTO;

@Mapper(componentModel = "spring")
public interface WebtoonMapper {
    WebtoonMapper INSTANCE = Mappers.getMapper(WebtoonMapper.class);

    @Mapping(source = "authors", target = "authors")
    @Mapping(source = "genres", target = "genres")
    WebtoonResponseDTO webtoonToWebtoonResponseDto(Webtoon webtoon);

    WebtoonUpdateRequestDTO webtoonToWebtoonUpdateRequestDTO(Webtoon webtoon);

}
