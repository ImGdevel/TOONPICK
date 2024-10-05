package toonpick.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import toonpick.app.dto.UserRatingDTO;
import toonpick.app.entity.UserRating;

@Mapper(componentModel = "spring")
public interface UserRatingMapper {
    UserRatingMapper INSTANCE = Mappers.getMapper(UserRatingMapper.class);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "webtoon", target = "webtoon")
    UserRatingDTO toDTO(UserRating userRating);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "webtoon", target = "webtoon")
    UserRating toEntity(UserRatingDTO userRatingDTO);
}
