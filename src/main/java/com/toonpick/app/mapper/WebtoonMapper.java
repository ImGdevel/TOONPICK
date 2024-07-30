package com.toonpick.app.mapper;

import com.toonpick.app.dto.WebtoonDTO;
import com.toonpick.app.entity.Webtoon;

public class WebtoonMapper {

    public WebtoonDTO toDTO(Webtoon webtoon) {
        return WebtoonDTO.builder()
                .id(webtoon.getId())
                .title(webtoon.getTitle())
                .author(webtoon.getAuthor())
                .genre(webtoon.getGenre())
                .description(webtoon.getDescription())
                .build();
    }

    public Webtoon toEntity(WebtoonDTO webtoonDTO) {
        return Webtoon.builder()
                .title(webtoonDTO.getTitle())
                .author(webtoonDTO.getAuthor())
                .genre(webtoonDTO.getGenre())
                .description(webtoonDTO.getDescription())
                .build();
    }
}
