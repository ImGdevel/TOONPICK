package com.toonpick.service;

import com.toonpick.dto.command.EpisodeRequest;
import com.toonpick.dto.command.WebtoonEpisodeUpdateCommand;
import com.toonpick.entity.Platform;
import com.toonpick.entity.Webtoon;
import com.toonpick.entity.WebtoonEpisode;
import com.toonpick.entity.WebtoonEpisodeLink;
import com.toonpick.enums.EpisodePricingType;
import com.toonpick.enums.EpisodeViewerType;
import com.toonpick.exception.EntityNotFoundException;
import com.toonpick.repository.PlatformRepository;
import com.toonpick.repository.WebtoonEpisodeLinkRepository;
import com.toonpick.repository.WebtoonEpisodeRepository;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WebtoonEpisodeUpdateService {

    private final WebtoonEpisodeRepository webtoonEpisodeRepository;
    private final WebtoonEpisodeLinkRepository webtoonEpisodeLinkRepository;
    private final WebtoonRepository webtoonRepository;
    private final PlatformRepository platformRepository;

    public void registerEpisodes(WebtoonEpisodeUpdateCommand result){
        Long webtoonId = result.getWebtoonId();
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.WEBTOON_NOT_FOUND));

        for(EpisodeRequest episodeRequest : result.getEpisodes()){
            createNewEpisode(webtoon, result.getPlatform(), episodeRequest);
        }
    }

    /**
     * 새로운 에피소드 등록
     */
    public void createNewEpisode(Webtoon webtoon, String platform, EpisodeRequest info){
        log.info("에피소드를 등록을 시도합니다.");
        WebtoonEpisode webtoonEpisode  = WebtoonEpisode.builder()
                .webtoon(webtoon)
                .title(info.getTitle())
                .episodeNumber(info.getEpisodeNumber())
                .pricingType(parsePricingType(info.getPricingType()))
                .build();

        webtoonEpisodeRepository.save(webtoonEpisode);
        
        // 웹 링크 등록
        if(isValidUrl(info.getWebUrl())){
            createWebtoonEpisodeLink(webtoonEpisode, info.getWebUrl(), platform, EpisodeViewerType.WEB);
        }
        // 모바일 링크 등록
        if(isValidUrl(info.getMobileUrl())){
            createWebtoonEpisodeLink(webtoonEpisode, info.getMobileUrl(), platform, EpisodeViewerType.MOBILE);
        }
    }

    private boolean isValidUrl(String url) {
        return url != null && !url.isEmpty() && !url.contains("null");
    }
    /**
     * 에피소드내 플랫폼 및 viewer애 따른 링크를 생성하고 등록합니다.
     */
    public void createWebtoonEpisodeLink(
            WebtoonEpisode episode,
            String url,
            String platformName,
            EpisodeViewerType viewerType
            ){
        Platform platform = platformRepository.findByName(platformName)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.PLATFORM_NOT_FOUND));

        WebtoonEpisodeLink webtoonEpisodeLink = WebtoonEpisodeLink.builder()
                .episode(episode)
                .url(url)
                .platform(platform)
                .viewerType(viewerType)
                .build();

        webtoonEpisodeLinkRepository.save(webtoonEpisodeLink);
    }

    /**
     * 구매 타입
     * @param pricingType
     * @return
     */
    private EpisodePricingType parsePricingType(String pricingType) {
        if (pricingType == null) {
            return EpisodePricingType.FREE;
        }
        try {
            return EpisodePricingType.valueOf(pricingType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return EpisodePricingType.FREE;
        }
    }
}
