package com.toonpick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.toonpick.dto.WebtoonResponseDTO;
import com.toonpick.dto.WebtoonUpdateRequestDTO;
import com.toonpick.entity.Webtoon;


@Mapper(componentModel = "spring")
public interface WebtoonMapper {
    WebtoonMapper INSTANCE = Mappers.getMapper(WebtoonMapper.class);

    @Mapping(source = "authors", target = "authors")
    @Mapping(source = "genres", target = "genres")
    WebtoonResponseDTO webtoonToWebtoonResponseDto(Webtoon webtoon);

    WebtoonUpdateRequestDTO webtoonToWebtoonUpdateRequestDTO(Webtoon webtoon);

}
