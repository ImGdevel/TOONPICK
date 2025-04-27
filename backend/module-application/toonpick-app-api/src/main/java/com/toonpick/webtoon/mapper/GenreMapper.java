package com.toonpick.webtoon.mapper;

import com.toonpick.dto.GenreDTO;
import com.toonpick.webtoon.response.GenreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.toonpick.entity.Genre;


@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    GenreDTO toGenreDTO(Genre genre);

    GenreResponse toGenreResponse(Genre genre);

}
