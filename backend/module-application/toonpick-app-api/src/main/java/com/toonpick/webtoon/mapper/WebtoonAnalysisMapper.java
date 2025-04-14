package com.toonpick.webtoon.mapper;

import com.toonpick.entity.WebtoonAnalysisDataDocument;
import com.toonpick.webtoon.response.WebtoonDetailsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WebtoonAnalysisMapper {
    WebtoonDetailsResponse.WebtoonAnalysisData toDto(WebtoonAnalysisDataDocument document);
}
