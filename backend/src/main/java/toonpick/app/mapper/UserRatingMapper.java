package toonpick.app.mapper;

import toonpick.app.dto.UserRatingDTO;
import toonpick.app.entity.UserRating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class, WebtoonMapper.class})
public interface UserRatingMapper {
    UserRatingMapper INSTANCE = Mappers.getMapper(UserRatingMapper.class);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "webtoon", target = "webtoon")
    UserRatingDTO reviewToReviewDto(UserRating userRating);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "webtoon", target = "webtoon")
    UserRating reviewDtoToReview(UserRatingDTO userRatingDTO);
}
