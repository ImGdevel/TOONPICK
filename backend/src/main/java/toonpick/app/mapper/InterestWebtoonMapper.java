package toonpick.app.mapper;

import toonpick.app.dto.InterestWebtoonDTO;
import toonpick.app.entity.InterestWebtoon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class, WebtoonMapper.class})
public interface InterestWebtoonMapper {
    InterestWebtoonMapper INSTANCE = Mappers.getMapper(InterestWebtoonMapper.class);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "webtoon", target = "webtoon")
    InterestWebtoonDTO interestWebtoonToInterestWebtoonDto(InterestWebtoon interestWebtoon);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "webtoon", target = "webtoon")
    InterestWebtoon interestWebtoonDtoToInterestWebtoon(InterestWebtoonDTO interestWebtoonDTO);
}