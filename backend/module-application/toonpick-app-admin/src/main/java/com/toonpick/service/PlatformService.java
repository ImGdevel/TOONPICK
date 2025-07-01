package com.toonpick.service;

import com.toonpick.domain.webtoon.entity.Platform;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.entity.WebtoonPlatform;
import com.toonpick.domain.webtoon.repository.PlatformRepository;
import com.toonpick.domain.webtoon.repository.WebtoonPlatformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlatformService {

    final private PlatformRepository platformRepository;
    final private WebtoonPlatformRepository webtoonPlatformRepository;

    /**
    웹툰에 플랫폼 등록
     */
    public void registerWebtoonPlatform(Webtoon webtoon, String platformName, String link) {
        Platform platform = findPlatform(platformName);
        createWebtoonPlatform(webtoon, platform, link);
    }

    public Platform findPlatform(String name){
        return platformRepository.findByName(name)
                .orElseGet(() -> platformRepository.save(Platform.builder().name(name).build()));
    }

    public WebtoonPlatform createWebtoonPlatform(Webtoon webtoon, Platform platform, String link){
        int currentRank = webtoonPlatformRepository.countByWebtoon(webtoon);

        WebtoonPlatform webtoonPlatform = WebtoonPlatform.builder()
                .webtoon(webtoon)
                .platform(platform)
                .link(link)
                .rank(currentRank + 1)
                .build();
        return webtoonPlatformRepository.save(webtoonPlatform);
    }
}
