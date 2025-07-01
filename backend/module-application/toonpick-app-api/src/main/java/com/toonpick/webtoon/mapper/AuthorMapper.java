package com.toonpick.webtoon.mapper;

import com.toonpick.domain.webtoon.entity.Author;
import com.toonpick.domain.webtoon.dto.AuthorDTO;
import com.toonpick.webtoon.response.AuthorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorDTO authorToAuthorDto(Author author);

    Author authorDtoToAuthor(AuthorDTO authorDTO);

    AuthorResponse toAuthorResponse(Author author);
}
