package com.toonpick.service;

import com.toonpick.dto.command.AuthorRequest;
import com.toonpick.dto.command.WebtoonCreateCommend;
import com.toonpick.entity.Author;
import com.toonpick.entity.Platform;
import com.toonpick.entity.Webtoon;
import com.toonpick.entity.WebtoonPlatform;
import com.toonpick.exception.DuplicateResourceException;
import com.toonpick.exception.EntityNotFoundException;
import com.toonpick.mapper.WebtoonMapper;
import com.toonpick.repository.PlatformRepository;
import com.toonpick.repository.WebtoonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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

        // 상황 -> 제목 검색 결과 없음
        when(webtoonRepository.findAllByTitle(request.getTitle()))
                .thenReturn(List.of());
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

        // 상황 -> 이미 존재하는 제목
        when(webtoonRepository.findAllByTitle(request.getTitle()))
                .thenReturn(List.of(existingWebtoon));

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
                "전지적 독자 시점", List.of("싱숑"), "KAKAO", "https://naver.com/1", 10);

        Platform existingPlatform = Platform.builder().name("NAVER").build();
        Webtoon existingWebtoon = Webtoon.builder()
                .title("전지적 독자 시점")
                .authors(Set.of(Author.builder().name("싱숑").build()))
                .platforms(new ArrayList<>(Set.of(
                        WebtoonPlatform.builder().platform(existingPlatform).build()
                )))
                .build();

        when(webtoonRepository.findAllByTitle(request.getTitle())).thenReturn(List.of(existingWebtoon));
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

        when(webtoonRepository.findAllByTitle(request.getTitle())).thenReturn(List.of());
        when(webtoonMapper.toWebtoon(request)).thenReturn(newWebtoon);
        when(platformRepository.findByName("NAVER")).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () ->
                webtoonRegistrationService.createWebtoon(request)
        );
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
                .status(com.toonpick.enums.SerializationStatus.ONGOING)
                .ageRating(com.toonpick.enums.AgeRating.ALL)
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
}
