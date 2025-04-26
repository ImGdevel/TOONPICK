package com.toonpick.mapper;

import com.toonpick.dto.request.WebtoonCreateRequest;
import com.toonpick.dto.request.WebtoonUpdateRequest;
import com.toonpick.entity.Webtoon;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WebtoonMapper {
    WebtoonMapper INSTANCE = Mappers.getMapper(WebtoonMapper.class);

    Webtoon toWebtoon(WebtoonCreateRequest webtoonCreateRequest);

    Webtoon toWebtoon(WebtoonUpdateRequest webtoonUpdateRequest);

}
