package com.toonpick.worker.domain.service;

import com.toonpick.worker.dto.command.EpisodeRequest;
import com.toonpick.worker.dto.command.WebtoonEpisodeUpdateCommand;
import com.toonpick.domain.webtoon.entity.Platform;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.entity.WebtoonEpisode;
import com.toonpick.domain.webtoon.entity.WebtoonEpisodeLink;
import com.toonpick.domain.webtoon.enums.EpisodePricingType;
import com.toonpick.domain.webtoon.enums.EpisodeViewerType;
import com.toonpick.common.exception.EntityNotFoundException;
import com.toonpick.domain.webtoon.repository.PlatformRepository;
import com.toonpick.domain.webtoon.repository.WebtoonEpisodeLinkRepository;
import com.toonpick.domain.webtoon.repository.WebtoonEpisodeRepository;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.common.type.ErrorCode;
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
    public void createNewEpisode(Webtoon webtoon, String platform, EpisodeRequest request){
        WebtoonEpisode webtoonEpisode  = WebtoonEpisode.builder()
                .webtoon(webtoon)
                .title(request.getTitle())
                .episodeNumber(request.getEpisodeNumber())
                .pricingType(EpisodePricingType.valueOf(request.getPricingType()))
                .build();

        webtoonEpisodeRepository.save(webtoonEpisode);
        
        // 웹 링크 등록
        if(isValidUrl(request.getWebUrl())){
            createWebtoonEpisodeLink(webtoonEpisode, request.getWebUrl(), platform, EpisodeViewerType.WEB);
        }
        // 모바일 링크 등록
        if(isValidUrl(request.getMobileUrl())){
            createWebtoonEpisodeLink(webtoonEpisode, request.getMobileUrl(), platform, EpisodeViewerType.MOBILE);
        }
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

    /*
    유효성 검사
     */
    private boolean isValidUrl(String url) {
        return url != null && !url.isEmpty() && !url.contains("null");
    }
}
