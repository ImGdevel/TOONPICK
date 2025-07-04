package com.toonpick.worker.integration.service;

import com.toonpick.common.exception.DuplicateResourceException;
import com.toonpick.common.exception.EntityNotFoundException;
import com.toonpick.domain.webtoon.entity.Platform;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.entity.WebtoonPlatform;
import com.toonpick.domain.webtoon.enums.AgeRating;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import com.toonpick.domain.webtoon.repository.PlatformRepository;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.worker.config.IntegrationTest;
import com.toonpick.worker.dto.command.AuthorRequest;
import com.toonpick.worker.dto.command.WebtoonCreateCommend;
import com.toonpick.worker.domain.service.WebtoonRegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@IntegrationTest
@DisplayName("Webtoon 등록 통합 테스트")
class WebtoonRegistrationServiceIntegrationTest {

    @Autowired
    private WebtoonRegistrationService webtoonRegistrationService;
    @Autowired
    private WebtoonRepository webtoonRepository;
    @Autowired 
    private PlatformRepository platformRepository;

    @BeforeEach
    void setUp() {
        webtoonRepository.deleteAll();
        platformRepository.deleteAll();

        platformRepository.save(Platform.builder().name("NAVER").build());
        platformRepository.save(Platform.builder().name("KAKAO").build());
    }


    @Test
    void 신규_웹툰_등록_및_플랫폼_연관관계_확인() {
        WebtoonCreateCommend request = createCommend("테스트웹툰", List.of("작가1"), "NAVER", "http://naver.com/1");

        webtoonRegistrationService.createWebtoon(request);

        List<Webtoon> found = webtoonRepository.findAllByTitle("테스트웹툰");
        assertThat(found).hasSize(1);
        Webtoon webtoon = found.get(0);
        assertThat(webtoon.getPlatforms()).hasSize(1);
        WebtoonPlatform platform = webtoon.getPlatforms().iterator().next();
        assertThat(platform.getPlatform().getName()).isEqualTo("NAVER");
        assertThat(platform.getLink()).isEqualTo("http://naver.com/1");
    }

    @Test
    void 동일_웹툰_다른_플랫폼_등록_시_플랫폼_추가됨() {
        WebtoonCreateCommend req1 = createCommend("테스트웹툰", List.of("작가1"), "NAVER", "http://naver.com/1");
        webtoonRegistrationService.createWebtoon(req1);

        WebtoonCreateCommend req2 = createCommend("테스트웹툰", List.of("작가1"), "KAKAO", "http://kakao.com/1");
        webtoonRegistrationService.createWebtoon(req2);

        List<Webtoon> found = webtoonRepository.findAllByTitle("테스트웹툰");
        assertThat(found).hasSize(1);
        Webtoon webtoon = found.get(0);
        assertThat(webtoon.getPlatforms()).hasSize(2);
        assertThat(webtoon.getPlatforms().stream().map(p -> p.getPlatform().getName()))
                .containsExactlyInAnyOrder("NAVER", "KAKAO");
    }

    @Test
    void 동일_플랫폼_중복_등록시_예외_발생() {
        WebtoonCreateCommend req = createCommend("테스트웹툰", List.of("작가1"), "NAVER", "https://naver.com/1");
        webtoonRegistrationService.createWebtoon(req);

        assertThatThrownBy(() -> webtoonRegistrationService.createWebtoon(req))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void 존재하지_않는_플랫폼_등록시_예외_발생() {
        WebtoonCreateCommend req = createCommend("테스트웹툰2", List.of("작가2"), "NOT_EXIST", "https://none.com/1");

        assertThatThrownBy(() -> webtoonRegistrationService.createWebtoon(req))
                .isInstanceOf(EntityNotFoundException.class);
    }


    private WebtoonCreateCommend createCommend(
            String title,
            List<String> authors,
            String platform,
            String url
    ) {
        List<AuthorRequest> authorRequests = authors.stream()
                .map(name -> AuthorRequest.builder()
                        .id("author_" + name)
                        .name(name)
                        .role("WRITER")
                        .build())
                .toList();

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
                .episodeCount(5)
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