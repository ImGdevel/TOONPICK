package toonpick.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import toonpick.app.dto.RecommendationListDTO;
import toonpick.app.entity.RecommendationList;
import toonpick.app.entity.Webtoon;
import toonpick.app.mapper.RecommendationListMapper;
import toonpick.app.repository.WebtoonRepository;

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
        // Given: Builder를 사용해 웹툰과 추천 리스트를 생성
        Webtoon webtoon1 = Webtoon.builder().id(1L).build();
        Webtoon webtoon2 = Webtoon.builder().id(2L).build();

        Set<Webtoon> webtoons = new HashSet<>(Arrays.asList(webtoon1, webtoon2));
        RecommendationList recommendationList = RecommendationList.builder()
            .theme("로맨스 웹툰 추천 10선")
            .description("10대 여성을 위한 로맨스 웹툰")
            .webtoons(webtoons)
            .build();

        // When: 추천 리스트를 DTO로 매핑
        RecommendationListDTO recommendationListDTO = recommendationListMapper.recommendationListToRecommendationListDTO(recommendationList);

        // Then: 매핑된 DTO의 웹툰 ID가 일치하는지 확인
        List<Long> expectedIds = Arrays.asList(1L, 2L);
        assertEquals(expectedIds, recommendationListDTO.getWebtoonIds());
    }

    @Test
    public void testRecommendationListDTOToRecommendationList() {
        // Given: 웹툰 ID 리스트와 함께 RecommendationListDTO를 생성
        List<Long> webtoonIds = Arrays.asList(1L, 2L);
        RecommendationListDTO recommendationListDTO = new RecommendationListDTO();
        recommendationListDTO.setWebtoonIds(webtoonIds);

        // Mock: 웹툰 ID로 웹툰을 찾는 동작을 모킹
        Webtoon webtoon1 = Webtoon.builder().id(1L).build();
        Webtoon webtoon2 = Webtoon.builder().id(2L).build();
        when(webtoonRepository.findAllById(webtoonIds)).thenReturn(Arrays.asList(webtoon1, webtoon2));

        // When: DTO를 엔티티로 매핑
        RecommendationList recommendationList = recommendationListMapper.recommendationListDTOToRecommendationList(recommendationListDTO, webtoonRepository);

        // Then: 매핑된 엔티티의 웹툰 세트가 일치하는지 확인
        Set<Webtoon> expectedWebtoons = new HashSet<>(Arrays.asList(webtoon1, webtoon2));
        assertEquals(expectedWebtoons, recommendationList.getWebtoons());
    }
}

