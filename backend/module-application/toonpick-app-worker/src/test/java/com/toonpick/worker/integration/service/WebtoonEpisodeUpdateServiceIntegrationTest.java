package com.toonpick.worker.integration.service;

import com.toonpick.common.exception.EntityNotFoundException;
import com.toonpick.domain.webtoon.entity.Platform;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.entity.WebtoonEpisode;
import com.toonpick.domain.webtoon.entity.WebtoonEpisodeLink;
import com.toonpick.domain.webtoon.enums.AgeRating;
import com.toonpick.domain.webtoon.enums.EpisodeViewerType;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import com.toonpick.domain.webtoon.repository.PlatformRepository;
import com.toonpick.domain.webtoon.repository.WebtoonEpisodeLinkRepository;
import com.toonpick.domain.webtoon.repository.WebtoonEpisodeRepository;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.worker.config.IntegrationTest;
import com.toonpick.worker.dto.command.EpisodeRequest;
import com.toonpick.worker.dto.command.WebtoonEpisodeUpdateCommand;
import com.toonpick.worker.service.WebtoonEpisodeUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@IntegrationTest
@DisplayName("WebtoonEpisodeUpdateService 통합 테스트")
class WebtoonEpisodeUpdateServiceIntegrationTest {

    @Autowired
    private WebtoonEpisodeUpdateService webtoonEpisodeUpdateService;
    @Autowired
    private WebtoonRepository webtoonRepository;
    @Autowired
    private PlatformRepository platformRepository;
    @Autowired
    private WebtoonEpisodeRepository webtoonEpisodeRepository;
    @Autowired
    private WebtoonEpisodeLinkRepository webtoonEpisodeLinkRepository;

    private Webtoon webtoon;

    @BeforeEach
    void setUp() {
        webtoonEpisodeLinkRepository.deleteAll();
        webtoonEpisodeRepository.deleteAll();
        webtoonRepository.deleteAll();
        platformRepository.deleteAll();

        Platform naver = platformRepository.save(Platform.builder().name("NAVER").build());
        Platform kakao = platformRepository.save(Platform.builder().name("KAKAO").build());

        webtoon = webtoonRepository.save(Webtoon.builder()
                .title("테스트웹툰")
                .dayOfWeek(java.time.DayOfWeek.MONDAY)
                .thumbnailUrl("https://thumbnail.com/image.jpg")
                .ageRating(AgeRating.ALL)
                .summary("웹툰 설명")
                .serializationStatus(SerializationStatus.ONGOING)
                .publishStartDate(java.time.LocalDate.parse("2023-01-01"))
                .lastUpdatedDate(java.time.LocalDate.parse("2024-01-01"))
                .build());
    }

    @Test
    void 에피소드_정상_등록_및_링크_확인() {
        EpisodeRequest episode = EpisodeRequest.builder()
                .title("1화")
                .episodeNumber(1)
                .pricingType("FREE")
                .webUrl("http://naver.com/ep1")
                .mobileUrl("http://m.naver.com/ep1")
                .build();
        WebtoonEpisodeUpdateCommand command = WebtoonEpisodeUpdateCommand.builder()
                .webtoonId(webtoon.getId())
                .platform("NAVER")
                .episodes(List.of(episode))
                .build();

        webtoonEpisodeUpdateService.registerEpisodes(command);

        List<WebtoonEpisode> episodes = webtoonEpisodeRepository.findByWebtoonIdOrderByEpisodeNumberAsc(webtoon.getId());
        assertThat(episodes).hasSize(1);
        WebtoonEpisode saved = episodes.get(0);
        assertThat(saved.getTitle()).isEqualTo("1화");
        assertThat(saved.getEpisodeNumber()).isEqualTo(1);
        List<WebtoonEpisodeLink> links = webtoonEpisodeLinkRepository.findAll();
        assertThat(links).hasSize(2);
        assertThat(links.stream().map(WebtoonEpisodeLink::getUrl)).containsExactlyInAnyOrder("http://naver.com/ep1", "http://m.naver.com/ep1");
        assertThat(links.stream().map(WebtoonEpisodeLink::getViewerType)).containsExactlyInAnyOrder(EpisodeViewerType.WEB, EpisodeViewerType.MOBILE);
    }

    @Test
    void 존재하지_않는_웹툰ID_등록시_예외_발생() {
        EpisodeRequest episode = EpisodeRequest.builder()
                .title("1화")
                .episodeNumber(1)
                .pricingType("FREE")
                .webUrl("http://naver.com/ep1")
                .mobileUrl("http://m.naver.com/ep1")
                .build();
        WebtoonEpisodeUpdateCommand command = WebtoonEpisodeUpdateCommand.builder()
                .webtoonId(-1L)
                .platform("NAVER")
                .episodes(List.of(episode))
                .build();

        assertThatThrownBy(() -> webtoonEpisodeUpdateService.registerEpisodes(command))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void 존재하지_않는_플랫폼_등록시_예외_발생() {
        EpisodeRequest episode = EpisodeRequest.builder()
                .title("1화")
                .episodeNumber(1)
                .pricingType("FREE")
                .webUrl("http://naver.com/ep1")
                .mobileUrl("http://m.naver.com/ep1")
                .build();
        WebtoonEpisodeUpdateCommand command = WebtoonEpisodeUpdateCommand.builder()
                .webtoonId(webtoon.getId())
                .platform("NOT_EXIST")
                .episodes(List.of(episode))
                .build();

        assertThatThrownBy(() -> webtoonEpisodeUpdateService.registerEpisodes(command))
                .isInstanceOf(EntityNotFoundException.class);
    }
} 