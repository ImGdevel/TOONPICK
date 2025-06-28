package com.toonpick.worker.unit.service;

import com.toonpick.domain.webtoon.entity.Author;
import com.toonpick.domain.webtoon.entity.Platform;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.entity.WebtoonPlatform;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.worker.config.UnitTest;
import com.toonpick.worker.dto.command.AuthorRequest;
import com.toonpick.worker.dto.command.WebtoonCreateCommend;
import com.toonpick.worker.domain.service.WebtoonDuplicateCheckService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@UnitTest
@DisplayName("WebtoonDuplicateCheckService 유닛 테스트")
class WebtoonDuplicateCheckServiceTest {

    @InjectMocks
    private WebtoonDuplicateCheckService duplicateCheckService;

    @Mock
    private WebtoonRepository webtoonRepository;

    @Test
    @DisplayName("동일한 제목과 작가의 웹툰이 없으면 빈 Optional을 반환한다")
    void findDuplicateWebtoon_중복_웹툰이_없으면_빈_Optional_반환() {
        // given
        WebtoonCreateCommend request = createCommend(
                "전지적 독자 시점", List.of("싱숑"), "NAVER", "https://naver.com/1", 10);

        // 상황 -> 제목으로 검색해도 결과 없음
        when(webtoonRepository.findExactMatchByTitleAndAuthors(
                request.getTitle(), List.of("싱숑"), 1))
                .thenReturn(Optional.empty());

        // when
        Optional<Webtoon> result = duplicateCheckService.findDuplicateWebtoon(request);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("동일한 제목과 작가의 웹툰이 있으면 해당 웹툰을 반환한다")
    void findDuplicateWebtoon_중복_웹툰이_있으면_해당_웹툰_반환() {
        // given
        WebtoonCreateCommend request = createCommend(
                "전지적 독자 시점", List.of("싱숑"), "NAVER", "https://naver.com/1", 10);

        Webtoon existingWebtoon = Webtoon.builder()
                .title("전지적 독자 시점")
                .authors(Set.of(Author.builder().name("싱숑").build()))
                .platforms(new ArrayList<>())
                .build();

        // 상황 -> 동일한 제목과 작가의 웹툰 존재
        when(webtoonRepository.findExactMatchByTitleAndAuthors(
                request.getTitle(), List.of("싱숑"), 1))
                .thenReturn(Optional.of(existingWebtoon));

        // when
        Optional<Webtoon> result = duplicateCheckService.findDuplicateWebtoon(request);

        // then
        assertTrue(result.isPresent());
        assertEquals("전지적 독자 시점", result.get().getTitle());
    }

    @Test
    @DisplayName("작가가 여러 명인 경우 모든 작가가 일치해야 중복으로 판단한다")
    void findDuplicateWebtoon_여러_작가가_모두_일치해야_중복_판단() {
        // given
        WebtoonCreateCommend request = createCommend(
                "전지적 독자 시점", List.of("싱숑", "김성철"), "NAVER", "https://naver.com/1", 10);

        Webtoon existingWebtoon = Webtoon.builder()
                .title("전지적 독자 시점")
                .authors(Set.of(
                        Author.builder().name("싱숑").build(),
                        Author.builder().name("김성철").build()
                ))
                .platforms(new ArrayList<>())
                .build();

        // 상황 -> 모든 작가가 일치하는 웹툰 존재
        when(webtoonRepository.findExactMatchByTitleAndAuthors(
                request.getTitle(), List.of("싱숑", "김성철"), 2))
                .thenReturn(Optional.of(existingWebtoon));

        // when
        Optional<Webtoon> result = duplicateCheckService.findDuplicateWebtoon(request);

        // then
        assertTrue(result.isPresent());
        assertEquals(2, result.get().getAuthors().size());
    }

    @Test
    @DisplayName("작가 정보가 없는 경우 제목만으로 중복 체크한다")
    void findDuplicateWebtoon_작가_정보가_없으면_제목만으로_체크() {
        // given
        WebtoonCreateCommend request = createCommend(
                "전지적 독자 시점", List.of(), "NAVER", "https://naver.com/1", 10);

        Webtoon existingWebtoon = Webtoon.builder()
                .title("전지적 독자 시점")
                .authors(new HashSet<>())
                .platforms(new ArrayList<>())
                .build();

        // 상황 -> 제목만으로 검색
        when(webtoonRepository.findAllByTitle(request.getTitle()))
                .thenReturn(List.of(existingWebtoon));

        // when
        Optional<Webtoon> result = duplicateCheckService.findDuplicateWebtoon(request);

        // then
        assertTrue(result.isPresent());
        assertEquals("전지적 독자 시점", result.get().getTitle());
    }

    @Test
    @DisplayName("특정 플랫폼이 이미 등록되어 있으면 true를 반환한다")
    void hasPlatform_플랫폼이_등록되어_있으면_true_반환() {
        // given
        Platform naverPlatform = Platform.builder().name("NAVER").build();
        Webtoon webtoon = Webtoon.builder()
                .title("전지적 독자 시점")
                .platforms(new ArrayList<>(List.of(
                        WebtoonPlatform.builder().platform(naverPlatform).build()
                )))
                .build();

        // when
        boolean result = duplicateCheckService.hasPlatform(webtoon, "NAVER");

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("특정 플랫폼이 등록되어 있지 않으면 false를 반환한다")
    void hasPlatform_플랫폼이_등록되어_있지_않으면_false_반환() {
        // given
        Platform naverPlatform = Platform.builder().name("NAVER").build();
        Webtoon webtoon = Webtoon.builder()
                .title("전지적 독자 시점")
                .platforms(new ArrayList<>(List.of(
                        WebtoonPlatform.builder().platform(naverPlatform).build()
                )))
                .build();

        // when
        boolean result = duplicateCheckService.hasPlatform(webtoon, "KAKAO");

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("웹툰에 플랫폼이 하나도 없으면 false를 반환한다")
    void hasPlatform_플랫폼이_없으면_false_반환() {
        // given
        Webtoon webtoon = Webtoon.builder()
                .title("전지적 독자 시점")
                .platforms(new ArrayList<>())
                .build();

        // when
        boolean result = duplicateCheckService.hasPlatform(webtoon, "NAVER");

        // then
        assertFalse(result);
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
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        return WebtoonCreateCommend.builder()
                .titleId("title_" + title)
                .title(title)
                .uniqueId("unique_" + title)
                .url(url)
                .platform(platform)
                .description("웹툰 설명")
                .thumbnailUrl("https://thumbnail.com/image.jpg")
                .dayOfWeek("MONDAY")
                .status(com.toonpick.domain.webtoon.enums.SerializationStatus.ONGOING)
                .ageRating(com.toonpick.domain.webtoon.enums.AgeRating.ALL)
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