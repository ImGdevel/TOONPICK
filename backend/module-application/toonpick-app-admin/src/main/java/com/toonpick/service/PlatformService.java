package com.toonpick.service;

import com.toonpick.entity.Platform;
import com.toonpick.entity.Webtoon;
import com.toonpick.entity.WebtoonPlatform;
import com.toonpick.repository.PlatformRepository;
import com.toonpick.repository.WebtoonPlatformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
        WebtoonPlatform webtoonPlatform = WebtoonPlatform.builder()
                .webtoon(webtoon)
                .platform(platform)
                .link(link)
                .build();
        return webtoonPlatformRepository.save(webtoonPlatform);
    }
}
