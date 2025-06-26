package com.toonpick.unit.service;

import com.toonpick.dto.command.EpisodeRequest;
import com.toonpick.dto.command.WebtoonEpisodeUpdateCommand;
import com.toonpick.domain.webtoon.entity.Platform;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.entity.WebtoonEpisode;
import com.toonpick.domain.webtoon.entity.WebtoonEpisodeLink;
import com.toonpick.domain.webtoon.enums.EpisodeViewerType;
import com.toonpick.common.exception.EntityNotFoundException;
import com.toonpick.domain.webtoon.repository.PlatformRepository;
import com.toonpick.domain.webtoon.repository.WebtoonEpisodeLinkRepository;
import com.toonpick.domain.webtoon.repository.WebtoonEpisodeRepository;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.service.WebtoonEpisodeUpdateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class WebtoonEpisodeUpdateServiceTest {

    @InjectMocks
    private WebtoonEpisodeUpdateService webtoonEpisodeUpdateService;

    @Mock
    private WebtoonEpisodeRepository webtoonEpisodeRepository;

    @Mock
    private WebtoonEpisodeLinkRepository webtoonEpisodeLinkRepository;

    @Mock
    private WebtoonRepository webtoonRepository;

    @Mock
    private PlatformRepository platformRepository;

    @Test
    @DisplayName("새로운 에피소드 생성에 성공하면 에피소드 및 웹/모바일 링크가 모두 저장된다")
    void registerEpisodes_성공하면_에피소드와_모든링크가_저장된다() {
        // given
        Long webtoonId = 1L;
        String platformName = "Naver";

        Webtoon webtoon = Webtoon.builder().id(webtoonId).build();
        Platform platform = Platform.builder().name(platformName).build();

        EpisodeRequest episodeRequest = EpisodeRequest.builder()
                .title("1화")
                .episodeNumber(1)
                .pricingType("FREE")
                .webUrl("http://test.com")
                .mobileUrl("http://test.com2")
                .build();

        WebtoonEpisodeUpdateCommand command = WebtoonEpisodeUpdateCommand.builder()
                .webtoonId(webtoonId)
                .platform(platformName)
                .episodes(List.of(episodeRequest))
                .build();

        when(webtoonRepository.findById(webtoonId)).thenReturn(Optional.of(webtoon));
        when(platformRepository.findByName(platformName)).thenReturn(Optional.of(platform));

        // when
        webtoonEpisodeUpdateService.registerEpisodes(command);

        // then
        // 에피소드 저장 검증
        ArgumentCaptor<WebtoonEpisode> episodeCaptor = ArgumentCaptor.forClass(WebtoonEpisode.class);
        verify(webtoonEpisodeRepository).save(episodeCaptor.capture());

        WebtoonEpisode savedEpisode = episodeCaptor.getValue();
        assertEquals("1화", savedEpisode.getTitle());
        assertEquals(1, savedEpisode.getEpisodeNumber());

        // 링크 저장 검증 - 총 2번 저장되어야 함
        ArgumentCaptor<WebtoonEpisodeLink> linkCaptor = ArgumentCaptor.forClass(WebtoonEpisodeLink.class);
        verify(webtoonEpisodeLinkRepository, times(2)).save(linkCaptor.capture());

        List<WebtoonEpisodeLink> savedLinks = linkCaptor.getAllValues();
        assertEquals(2, savedLinks.size());

        // 각 링크 검증
        WebtoonEpisodeLink webLink = savedLinks.stream()
                .filter(link -> link.getViewerType() == EpisodeViewerType.WEB)
                .findFirst().orElseThrow();

        WebtoonEpisodeLink mobileLink = savedLinks.stream()
                .filter(link -> link.getViewerType() == EpisodeViewerType.MOBILE)
                .findFirst().orElseThrow();

        assertEquals("http://test.com", webLink.getUrl());
        assertEquals("http://test.com2", mobileLink.getUrl());
        assertEquals(platform, webLink.getPlatform());
        assertEquals(platform, mobileLink.getPlatform());
    }


    @Test
    @DisplayName("웹툰 ID가 존재하지 않으면 예외가 발생한다")
    void registerEpisodes_웹툰이_존재하지_않으면_예외가_발생한다() {
        // given
        Long webtoonId = 999L;
        EpisodeRequest episodeRequest = EpisodeRequest.builder()
                .title("제목")
                .episodeNumber(1)
                .pricingType("FREE")
                .webUrl("http://test.com")
                .mobileUrl("http://test.com2")
                .build();

        WebtoonEpisodeUpdateCommand command = WebtoonEpisodeUpdateCommand.builder()
                .webtoonId(webtoonId)
                .platform("Kakao")
                .episodes(List.of(episodeRequest))
                .build();

        when(webtoonRepository.findById(webtoonId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () ->
                webtoonEpisodeUpdateService.registerEpisodes(command)
        );

        verify(webtoonEpisodeRepository, never()).save(any());
        verify(webtoonEpisodeLinkRepository, never()).save(any());
    }

    @Test
    @DisplayName("플랫폼 이름이 유효하지 않으면 링크 생성 시 예외가 발생한다")
    void createWebtoonEpisodeLink_플랫폼이_존재하지_않으면_예외가_발생한다() {
        // given
        WebtoonEpisode dummyEpisode = WebtoonEpisode.builder().build();
        String invalidPlatform = "UnknownPlatform";

        when(platformRepository.findByName(invalidPlatform)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () ->
                webtoonEpisodeUpdateService.createWebtoonEpisodeLink(
                        dummyEpisode, "http://test.com", invalidPlatform, EpisodeViewerType.MOBILE
                )
        );

        verify(webtoonEpisodeLinkRepository, never()).save(any());
    }
}
