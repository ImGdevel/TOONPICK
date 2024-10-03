package toonpick.app.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import toonpick.app.dto.RecommendationListDTO;
import toonpick.app.entity.RecommendationList;
import toonpick.app.entity.Webtoon;
import toonpick.app.repository.WebtoonRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RecommendationListMapper {

    RecommendationListMapper INSTANCE = Mappers.getMapper(RecommendationListMapper.class);

    @Mapping(source = "webtoons", target = "webtoonIds")
    RecommendationListDTO recommendationListToRecommendationListDTO(RecommendationList recommendationList);

    @Mapping(source = "webtoonIds", target = "webtoons")
    RecommendationList recommendationListDTOToRecommendationList(RecommendationListDTO recommendationListDTO, @Context WebtoonRepository webtoonRepository);

    default List<Long> mapWebtoonsToIds(Set<Webtoon> webtoons) {
        return webtoons.stream()
                .map(Webtoon::getId)
                .collect(Collectors.toList());
    }

    default Set<Webtoon> mapIdsToWebtoons(List<Long> ids, @Context WebtoonRepository webtoonRepository) {
        return new HashSet<>(webtoonRepository.findAllById(ids));
    }
}
