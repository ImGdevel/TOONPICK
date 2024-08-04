package toonpick.app.mapper;

import toonpick.app.dto.GenreDTO;
import toonpick.app.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    GenreDTO genreToGenreDto(Genre genre);

    Genre genreDtoToGenre(GenreDTO genreDTO);
}