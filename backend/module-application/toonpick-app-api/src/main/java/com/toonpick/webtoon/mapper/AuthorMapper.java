package com.toonpick.webtoon.mapper;

import com.toonpick.entity.Author;
import com.toonpick.dto.AuthorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorDTO authorToAuthorDto(Author author);

    Author authorDtoToAuthor(AuthorDTO authorDTO);
}
