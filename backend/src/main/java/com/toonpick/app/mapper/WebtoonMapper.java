package com.toonpick.app.mapper;

import com.toonpick.app.dto.WebtoonDTO;
import com.toonpick.app.entity.Webtoon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WebtoonMapper {
    WebtoonMapper INSTANCE = Mappers.getMapper(WebtoonMapper.class);

    @Mapping(source = "authors", target = "authors")
    @Mapping(source = "genres", target = "genres")
    WebtoonDTO webtoonToWebtoonDto(Webtoon webtoon);

    @Mapping(source = "authors", target = "authors")
    @Mapping(source = "genres", target = "genres")
    Webtoon webtoonDtoToWebtoon(WebtoonDTO webtoonDto);
}