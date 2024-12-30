package toonpick.app.unit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import toonpick.app.entity.RecommendationList;
import toonpick.app.webtoon.entity.Webtoon;
import toonpick.app.mapper.RecommendationListMapper;
import toonpick.app.webtoon.repository.WebtoonRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class RecommendationListMapperTest {

    @InjectMocks
    private RecommendationListMapper recommendationListMapper = Mappers.getMapper(RecommendationListMapper.class);

    @Mock
    private WebtoonRepository webtoonRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRecommendationListToRecommendationListDTO() {
        Webtoon webtoon1 = Webtoon.builder().id(1L).build();
        Webtoon webtoon2 = Webtoon.builder().id(2L).build();

        Set<Webtoon> webtoons = new HashSet<>(Arrays.asList(webtoon1, webtoon2));
        RecommendationList recommendationList = RecommendationList.builder()
            .theme("로맨스 웹툰 추천 10선")
            .description("10대 여성을 위한 로맨스 웹툰")
            .webtoons(webtoons)
            .build();

        RecommendationListDTO recommendationListDTO = recommendationListMapper.recommendationListToRecommendationListDTO(recommendationList);

        List<Long> expectedIds = Arrays.asList(1L, 2L);
        assertEquals(expectedIds, recommendationListDTO.getWebtoonIds());
    }

    @Test
    public void testRecommendationListDTOToRecommendationList() {

        List<Long> webtoonIds = Arrays.asList(1L, 2L);
        RecommendationListDTO recommendationListDTO = new RecommendationListDTO();
        recommendationListDTO.setWebtoonIds(webtoonIds);

        Webtoon webtoon1 = Webtoon.builder().id(1L).build();
        Webtoon webtoon2 = Webtoon.builder().id(2L).build();
        when(webtoonRepository.findAllById(webtoonIds)).thenReturn(Arrays.asList(webtoon1, webtoon2));


        RecommendationList recommendationList = recommendationListMapper.recommendationListDTOToRecommendationList(recommendationListDTO, webtoonRepository);

        Set<Webtoon> expectedWebtoons = new HashSet<>(Arrays.asList(webtoon1, webtoon2));
        assertEquals(expectedWebtoons, recommendationList.getWebtoons());
    }
}

