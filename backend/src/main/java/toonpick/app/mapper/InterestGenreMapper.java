package toonpick.app.mapper;

import toonpick.app.dto.InterestGenreDTO;
import toonpick.app.entity.InterestGenre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class, GenreMapper.class})
public interface InterestGenreMapper {
    InterestGenreMapper INSTANCE = Mappers.getMapper(InterestGenreMapper.class);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "genre", target = "genre")
    InterestGenreDTO interestGenreToInterestGenreDto(InterestGenre interestGenre);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "genre", target = "genre")
    InterestGenre interestGenreDtoToInterestGenre(InterestGenreDTO interestGenreDTO);
}