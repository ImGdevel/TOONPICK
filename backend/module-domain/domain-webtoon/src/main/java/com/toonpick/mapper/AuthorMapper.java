package com.toonpick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.toonpick.dto.AuthorDTO;
import com.toonpick.entity.Author;


@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorDTO authorToAuthorDto(Author author);

    Author authorDtoToAuthor(AuthorDTO authorDTO);
}
