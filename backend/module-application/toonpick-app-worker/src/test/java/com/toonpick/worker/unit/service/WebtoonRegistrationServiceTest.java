package com.toonpick.worker.unit.service;

import com.toonpick.common.exception.DuplicateResourceException;
import com.toonpick.common.exception.EntityNotFoundException;
import com.toonpick.domain.webtoon.entity.Author;
import com.toonpick.domain.webtoon.entity.Platform;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.entity.WebtoonPlatform;
import com.toonpick.domain.webtoon.enums.AgeRating;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import com.toonpick.domain.webtoon.repository.PlatformRepository;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.worker.config.UnitTest;
import com.toonpick.worker.dto.command.AuthorRequest;
import com.toonpick.worker.dto.command.WebtoonCreateCommend;
import com.toonpick.worker.mapper.WebtoonMapper;
import com.toonpick.worker.domain.service.WebtoonEpisodeUpdateService;
import com.toonpick.worker.domain.service.WebtoonRegistrationService;
import com.toonpick.worker.domain.service.WebtoonDuplicateCheckService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
@DisplayName("WebtoonRegistrationService 유닛 테스트")
class WebtoonRegistrationServiceTest {

    @InjectMocks
    private WebtoonRegistrationService webtoonRegistrationService;

    @Mock
    private WebtoonRepository webtoonRepository;

    @Mock
    private WebtoonMapper webtoonMapper;

    @Mock
    private PlatformRepository platformRepository;

    @Mock
    private WebtoonEpisodeUpdateService webtoonEpisodeUpdateService;

    @Mock
    private WebtoonDuplicateCheckService duplicateCheckService;

    @Test
    @DisplayName("동일한 제목과 작가의 웹툰이 없을 경우 새 웹툰과 플랫폼을 등록한다")
    void createWebtoon_신규_웹툰이면_웹툰과_플랫폼_등록한다() {
        // given
        WebtoonCreateCommend request = createCommend(
                "전지적 독자 시점", List.of("싱숑"), "NAVER", "https://naver.com/1", 10);

        Webtoon newWebtoon = Webtoon.builder()
                .title(request.getTitle())
                .authors(new HashSet<>())
                .platforms(new ArrayList<>())
                .build();
        Platform platform = Platform.builder()
                .name("NAVER")
                .build();

        // 상황 -> 중복 체크 결과 없음
        when(duplicateCheckService.findDuplicateWebtoon(request))
                .thenReturn(Optional.empty());
        when(webtoonMapper.toWebtoon(request))
                .thenReturn(newWebtoon);
        when(platformRepository.findByName("NAVER"))
                .thenReturn(Optional.of(platform));

        // when
        webtoonRegistrationService.createWebtoon(request);

        // then
        verify(webtoonRepository).save(newWebtoon);
        assertEquals(1, newWebtoon.getPlatforms().size());
    }

    @Test
    @DisplayName("동일한 웹툰이 존재하고 플랫폼이 동일하면 예외가 발생한다")
    void createWebtoon_동일한_웹툰과_플랫폼이_존재하면_예외_발생() {
        // given
        WebtoonCreateCommend request = createCommend(
                "전지적 독자 시점", List.of("싱숑"), "NAVER", "https://naver.com/1", 10);

        Platform existingPlatform = Platform.builder().name("NAVER").build();
        Webtoon existingWebtoon = Webtoon.builder()
                .title("전지적 독자 시점")
                .authors(Set.of(Author.builder().name("싱숑").build()))
                .platforms(new ArrayList<>(Set.of(
                        WebtoonPlatform.builder().platform(existingPlatform).build()
                )))
                .build();

        // 상황 -> 중복 웹툰 존재하고 동일한 플랫폼
        when(duplicateCheckService.findDuplicateWebtoon(request))
                .thenReturn(Optional.of(existingWebtoon));
        when(duplicateCheckService.hasPlatform(existingWebtoon, "NAVER"))
                .thenReturn(true);

        // when & then
        assertThrows(DuplicateResourceException.class, () ->
                webtoonRegistrationService.createWebtoon(request)
        );
    }

    @Test
    @DisplayName("동일한 웹툰이 존재하지만 플랫폼이 다르면 플랫폼이 추가된다")
    void createWebtoon_동일한_웹툰이지만_다른_플랫폼이면_플랫폼_추가된다() {
        // given
        WebtoonCreateCommend request = createCommend(
                "전지적 독자 시점", List.of("싱숑"), "KAKAO", "https://kakao.com/1", 10);

        Platform existingPlatform = Platform.builder().name("NAVER").build();
        Webtoon existingWebtoon = Webtoon.builder()
                .title("전지적 독자 시점")
                .authors(Set.of(Author.builder().name("싱숑").build()))
                .platforms(new ArrayList<>(Set.of(
                        WebtoonPlatform.builder().platform(existingPlatform).build()
                )))
                .build();

        // 상황 -> 중복 웹툰 존재하지만 다른 플랫폼
        when(duplicateCheckService.findDuplicateWebtoon(request))
                .thenReturn(Optional.of(existingWebtoon));
        when(duplicateCheckService.hasPlatform(existingWebtoon, "KAKAO"))
                .thenReturn(false);
        
        Platform newPlatform = Platform.builder().name("KAKAO").build();
        when(platformRepository.findByName("KAKAO")).thenReturn(Optional.of(newPlatform));

        // when
        webtoonRegistrationService.createWebtoon(request);

        // then
        assertTrue(existingWebtoon.getPlatforms().stream()
                .anyMatch(p -> p.getPlatform().getName().equals("KAKAO")));
    }

    @Test
    @DisplayName("플랫폼이 존재하지 않으면 예외가 발생한다")
    void createWebtoon_플랫폼이_존재하지_않으면_예외_발생() {
        // given
        WebtoonCreateCommend request = createCommend(
                "전지적 독자 시점", List.of("싱숑"), "NAVER", "https://naver.com/1", 10);

        Webtoon newWebtoon = Webtoon.builder()
                .title(request.getTitle())
                .authors(new HashSet<>())
                .platforms(new ArrayList<>())
                .build();

        when(duplicateCheckService.findDuplicateWebtoon(request)).thenReturn(Optional.empty());
        when(webtoonMapper.toWebtoon(request)).thenReturn(newWebtoon);
        when(platformRepository.findByName("NAVER")).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () ->
                webtoonRegistrationService.createWebtoon(request)
        );
    }

    @Test
    @DisplayName("중복 웹툰이 존재하지만 플랫폼이 존재하지 않으면 예외가 발생한다")
    void createWebtoon_중복_웹툰이지만_플랫폼이_존재하지_않으면_예외_발생() {
        // given
        WebtoonCreateCommend request = createCommend(
                "전지적 독자 시점", List.of("싱숑"), "UNKNOWN_PLATFORM", "https://unknown.com/1", 10);

        Webtoon existingWebtoon = Webtoon.builder()
                .title("전지적 독자 시점")
                .authors(Set.of(Author.builder().name("싱숑").build()))
                .platforms(new ArrayList<>())
                .build();

        // 상황 -> 중복 웹툰 존재하지만 플랫폼이 존재하지 않음
        when(duplicateCheckService.findDuplicateWebtoon(request))
                .thenReturn(Optional.of(existingWebtoon));
        when(duplicateCheckService.hasPlatform(existingWebtoon, "UNKNOWN_PLATFORM"))
                .thenReturn(false);
        when(platformRepository.findByName("UNKNOWN_PLATFORM")).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () ->
                webtoonRegistrationService.createWebtoon(request)
        );
    }

    @Test
    @DisplayName("에피소드가 있는 경우 에피소드도 함께 등록된다")
    void createWebtoon_에피소드가_있으면_에피소드도_등록된다() {
        // given
        WebtoonCreateCommend request = createCommendWithEpisodes(
                "전지적 독자 시점", List.of("싱숑"), "NAVER", "https://naver.com/1", 10);

        Webtoon newWebtoon = Webtoon.builder()
                .title(request.getTitle())
                .authors(new HashSet<>())
                .platforms(new ArrayList<>())
                .build();
        Platform platform = Platform.builder().name("NAVER").build();

        when(duplicateCheckService.findDuplicateWebtoon(request)).thenReturn(Optional.empty());
        when(webtoonMapper.toWebtoon(request)).thenReturn(newWebtoon);
        when(platformRepository.findByName("NAVER")).thenReturn(Optional.of(platform));

        // when
        webtoonRegistrationService.createWebtoon(request);

        // then
        verify(webtoonRepository).save(newWebtoon);
        verify(webtoonEpisodeUpdateService).createNewEpisode(newWebtoon, "NAVER", request.getEpisodes().get(0));
        verify(webtoonEpisodeUpdateService).createNewEpisode(newWebtoon, "NAVER", request.getEpisodes().get(1));
    }

    // WebtoonCommend 생성 메서드
    private WebtoonCreateCommend createCommend(
            String title,
            List<String> authors,
            String platform,
            String url,
            Integer episodeCount
    ) {
        List<AuthorRequest> authorRequests = authors.stream()
                .map(name -> AuthorRequest.builder()
                        .id("author_" + name)
                        .name(name)
                        .role("WRITER")
                        .build())
                .collect(Collectors.toList());

        return WebtoonCreateCommend.builder()
                .titleId("title_" + title)
                .title(title)
                .uniqueId("unique_" + title)
                .url(url)
                .platform(platform)
                .description("웹툰 설명")
                .thumbnailUrl("https://thumbnail.com/image.jpg")
                .dayOfWeek("MONDAY")
                .status(SerializationStatus.ONGOING)
                .ageRating(AgeRating.ALL)
                .episodeCount(episodeCount)
                .previewCount(0)
                .genres(List.of("액션", "판타지"))
                .authors(authorRequests)
                .publishStartDate("2023-01-01")
                .lastUpdatedDate("2024-01-01")
                .episodes(List.of())
                .relatedNovels(List.of())
                .relatedWebtoonIds(List.of())
                .build();
    }

    // WebtoonCommend 생성 메서드 (에피소드 포함)
    private WebtoonCreateCommend createCommendWithEpisodes(
            String title,
            List<String> authors,
            String platform,
            String url,
            Integer episodeCount
    ) {
        List<AuthorRequest> authorRequests = authors.stream()
                .map(name -> AuthorRequest.builder()
                        .id("author_" + name)
                        .name(name)
                        .role("WRITER")
                        .build())
                .collect(Collectors.toList());

        return WebtoonCreateCommend.builder()
                .titleId("title_" + title)
                .title(title)
                .uniqueId("unique_" + title)
                .url(url)
                .platform(platform)
                .description("웹툰 설명")
                .thumbnailUrl("https://thumbnail.com/image.jpg")
                .dayOfWeek("MONDAY")
                .status(SerializationStatus.ONGOING)
                .ageRating(AgeRating.ALL)
                .episodeCount(episodeCount)
                .previewCount(0)
                .genres(List.of("액션", "판타지"))
                .authors(authorRequests)
                .publishStartDate("2023-01-01")
                .lastUpdatedDate("2024-01-01")
                .episodes(List.of(
                    createEpisodeRequest("1화", "https://episode1.com", 1),
                    createEpisodeRequest("2화", "https://episode2.com", 2)
                ))
                .relatedNovels(List.of())
                .relatedWebtoonIds(List.of())
                .build();
    }

    // EpisodeRequest 생성 메서드
    private com.toonpick.worker.dto.command.EpisodeRequest createEpisodeRequest(
            String title, String url, int episodeNumber) {
        return com.toonpick.worker.dto.command.EpisodeRequest.builder()
                .title(title)
                .webUrl(url)
                .episodeNumber(episodeNumber)
                .pricingType("FREE")
                .build();
    }
}
