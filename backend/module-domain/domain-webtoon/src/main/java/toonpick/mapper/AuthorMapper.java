package toonpick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import toonpick.dto.AuthorDTO;
import toonpick.entity.Author;


@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorDTO authorToAuthorDto(Author author);

    Author authorDtoToAuthor(AuthorDTO authorDTO);
}
