package com.toonpick.service;

import com.toonpick.dto.command.WebtoonEpisodeUpdateCommend;
import com.toonpick.entity.Platform;
import com.toonpick.entity.Webtoon;
import com.toonpick.entity.WebtoonEpisode;
import com.toonpick.entity.WebtoonEpisodeLink;
import com.toonpick.enums.EpisodeViewerType;
import com.toonpick.exception.EntityNotFoundException;
import com.toonpick.repository.PlatformRepository;
import com.toonpick.repository.WebtoonEpisodeLinkRepository;
import com.toonpick.repository.WebtoonEpisodeRepository;
import com.toonpick.repository.WebtoonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("새로운 에피소드 생성에 성공하면 에피소드 및 링크가 저장된다")
    void createNewEpisode_성공하면_에피소드와_링크가_저장된다() {
        // given
        Long webtoonId = 1L;
        String platformName = "Naver";
        String viewerType = "WEB";

        Webtoon webtoon = Webtoon.builder().id(webtoonId).build();
        Platform platform = Platform.builder().name(platformName).build();

        WebtoonEpisodeUpdateCommend commend = WebtoonEpisodeUpdateCommend.builder()
                .webtoonId(webtoonId)
                .title("1화")
                .episodeNumber(1)
                .pricingType("FREE")
                .url("http://test.com")
                .platform(platformName)
                .viewerType(viewerType)
                .build();

        when(webtoonRepository.findById(webtoonId)).thenReturn(Optional.of(webtoon));
        when(platformRepository.findByName(platformName)).thenReturn(Optional.of(platform));

        // when
        webtoonEpisodeUpdateService.createNewEpisode(commend);

        // then
        ArgumentCaptor<WebtoonEpisode> episodeCaptor = ArgumentCaptor.forClass(WebtoonEpisode.class);
        verify(webtoonEpisodeRepository).save(episodeCaptor.capture());

        WebtoonEpisode savedEpisode = episodeCaptor.getValue();
        assertEquals("1화", savedEpisode.getTitle());
        assertEquals(1, savedEpisode.getEpisodeNumber());

        ArgumentCaptor<WebtoonEpisodeLink> linkCaptor = ArgumentCaptor.forClass(WebtoonEpisodeLink.class);
        verify(webtoonEpisodeLinkRepository).save(linkCaptor.capture());

        WebtoonEpisodeLink savedLink = linkCaptor.getValue();
        assertEquals("http://test.com", savedLink.getUrl());
        assertEquals(platform, savedLink.getPlatform());
        assertEquals(EpisodeViewerType.valueOf(viewerType), savedLink.getViewerType());
    }

    @Test
    @DisplayName("웹툰 ID가 존재하지 않으면 예외가 발생한다")
    void createNewEpisode_웹툰이_존재하지_않으면_예외가_발생한다() {
        // given
        Long webtoonId = 999L;
        WebtoonEpisodeUpdateCommend commend = WebtoonEpisodeUpdateCommend.builder()
                .webtoonId(webtoonId)
                .title("제목")
                .episodeNumber(1)
                .pricingType("FREE")
                .url("http://test.com")
                .platform("Kakao")
                .viewerType("PAGE")
                .build();

        when(webtoonRepository.findById(webtoonId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () ->
                webtoonEpisodeUpdateService.createNewEpisode(commend)
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
                        dummyEpisode, "http://test.com", invalidPlatform, "SCROLL"
                )
        );

        verify(webtoonEpisodeLinkRepository, never()).save(any());
    }
}
