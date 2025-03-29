package toonpick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import toonpick.dto.GenreDTO;
import toonpick.entity.Genre;


@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    GenreDTO genreToGenreDto(Genre genre);

    Genre genreDtoToGenre(GenreDTO genreDTO);
}
