package com.toonpick.webtoon.mapper;

import com.toonpick.domain.webtoon.entity.WebtoonPlatform;
import com.toonpick.webtoon.response.PlatformResponse;
import com.toonpick.webtoon.response.WebtoonDetailsResponse;
import com.toonpick.webtoon.response.WebtoonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.toonpick.domain.webtoon.entity.Webtoon;


@Mapper(componentModel = "spring")
public interface WebtoonMapper {
    WebtoonMapper INSTANCE = Mappers.getMapper(WebtoonMapper.class);

    @Mapping(source = "authors", target = "authors")
    @Mapping(source = "genres", target = "genres")
    WebtoonResponse toWebtoonResponse(Webtoon webtoon);

    WebtoonDetailsResponse toWebtoonDetailsResponse(Webtoon webtoon);

    @Mapping(target = "name", source = "platform.name")
    @Mapping(target = "link", source = "link")
    PlatformResponse toPlatformResponse(WebtoonPlatform webtoonPlatform);
}
