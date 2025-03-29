package toonpick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import toonpick.dto.WebtoonResponseDTO;
import toonpick.dto.WebtoonUpdateRequestDTO;
import toonpick.entity.Webtoon;


@Mapper(componentModel = "spring")
public interface WebtoonMapper {
    WebtoonMapper INSTANCE = Mappers.getMapper(WebtoonMapper.class);

    @Mapping(source = "authors", target = "authors")
    @Mapping(source = "genres", target = "genres")
    WebtoonResponseDTO webtoonToWebtoonResponseDto(Webtoon webtoon);

    WebtoonUpdateRequestDTO webtoonToWebtoonUpdateRequestDTO(Webtoon webtoon);

}
