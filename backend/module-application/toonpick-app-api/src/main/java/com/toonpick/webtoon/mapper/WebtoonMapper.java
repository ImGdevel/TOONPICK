package com.toonpick.webtoon.mapper;

import com.toonpick.webtoon.response.WebtoonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.toonpick.entity.Webtoon;


@Mapper(componentModel = "spring")
public interface WebtoonMapper {
    WebtoonMapper INSTANCE = Mappers.getMapper(WebtoonMapper.class);

    @Mapping(source = "authors", target = "authors")
    @Mapping(source = "genres", target = "genres")
    WebtoonResponse webtoonToWebtoonResponseDto(Webtoon webtoon);


}
