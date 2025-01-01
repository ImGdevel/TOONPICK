package toonpick.app.webtoon.mapper;

import toonpick.app.webtoon.dto.GenreDTO;
import toonpick.app.webtoon.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    GenreDTO genreToGenreDto(Genre genre);

    Genre genreDtoToGenre(GenreDTO genreDTO);
}
