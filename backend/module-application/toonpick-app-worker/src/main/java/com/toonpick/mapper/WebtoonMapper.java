package com.toonpick.mapper;

import com.toonpick.dto.request.WebtoonCreateCommand;
import com.toonpick.dto.request.WebtoonUpdatePayload;
import com.toonpick.entity.Webtoon;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface WebtoonMapper {
    WebtoonMapper INSTANCE = Mappers.getMapper(WebtoonMapper.class);

    Webtoon toWebtoon(WebtoonCreateCommand request);

    WebtoonUpdatePayload toWebtoonUpdatePayload(Webtoon webtoon);

}
