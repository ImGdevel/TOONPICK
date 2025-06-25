package com.toonpick.service;

import com.toonpick.dto.command.AuthorRequest;
import com.toonpick.dto.command.WebtoonCreateCommend;
import com.toonpick.entity.Platform;
import com.toonpick.entity.Webtoon;
import com.toonpick.entity.WebtoonPlatform;
import com.toonpick.exception.DuplicateResourceException;
import com.toonpick.exception.EntityNotFoundException;
import com.toonpick.repository.PlatformRepository;
import com.toonpick.repository.WebtoonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class WebtoonRegistrationServiceIntegrationTest {

    @Autowired
    private WebtoonRegistrationService webtoonRegistrationService;
    @Autowired
    private WebtoonRepository webtoonRepository;
    @Autowired
    private PlatformRepository platformRepository;

    @BeforeEach
    void setUp() {
        // 테스트용 플랫폼 미리 저장
        platformRepository.save(Platform.builder().name("NAVER").build());
        platformRepository.save(Platform.builder().name("KAKAO").build());
    }

    @Test
    @DisplayName("신규 웹툰 등록 - DB에 저장 및 플랫폼 연관관계 확인")
    void createWebtoon_newWebtoon_success() {
        WebtoonCreateCommend request = createCommend("테스트웹툰", List.of("작가1"), "NAVER", "https://naver.com/1", 5);

        webtoonRegistrationService.createWebtoon(request);

        List<Webtoon> found = webtoonRepository.findAllByTitle("테스트웹툰");
        assertThat(found).hasSize(1);
        Webtoon webtoon = found.get(0);
        assertThat(webtoon.getPlatforms()).hasSize(1);
        WebtoonPlatform platform = webtoon.getPlatforms().iterator().next();
        assertThat(platform.getPlatform().getName()).isEqualTo("NAVER");
        assertThat(platform.getLink()).isEqualTo("https://naver.com/1");
    }

    @Test
    @DisplayName("동일 웹툰에 다른 플랫폼 추가 - 플랫폼 추가됨 확인")
    void createWebtoon_sameTitleAuthor_addPlatform() {
        // 1차 등록 (NAVER)
        WebtoonCreateCommend req1 = createCommend("테스트웹툰", List.of("작가1"), "NAVER", "https://naver.com/1", 5);
        webtoonRegistrationService.createWebtoon(req1);
        // 2차 등록 (KAKAO)
        WebtoonCreateCommend req2 = createCommend("테스트웹툰", List.of("작가1"), "KAKAO", "https://kakao.com/1", 5);
        webtoonRegistrationService.createWebtoon(req2);

        List<Webtoon> found = webtoonRepository.findAllByTitle("테스트웹툰");
        assertThat(found).hasSize(1);
        Webtoon webtoon = found.get(0);
        assertThat(webtoon.getPlatforms()).hasSize(2);
        assertThat(webtoon.getPlatforms().stream().map(p -> p.getPlatform().getName()).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder("NAVER", "KAKAO");
    }

    @Test
    @DisplayName("동일 웹툰+플랫폼 중복 등록 시 예외 발생")
    void createWebtoon_duplicateWebtoonPlatform_throwsException() {
        WebtoonCreateCommend req = createCommend("테스트웹툰", List.of("작가1"), "NAVER", "https://naver.com/1", 5);
        webtoonRegistrationService.createWebtoon(req);
        assertThatThrownBy(() -> webtoonRegistrationService.createWebtoon(req))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("존재하지 않는 플랫폼 등록 시 예외 발생")
    void createWebtoon_platformNotFound_throwsException() {
        WebtoonCreateCommend req = createCommend("테스트웹툰2", List.of("작가2"), "NOT_EXIST", "https://none.com/1", 5);
        assertThatThrownBy(() -> webtoonRegistrationService.createWebtoon(req))
                .isInstanceOf(EntityNotFoundException.class);
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