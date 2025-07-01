package com.toonpick.worker.domain.service;

import com.toonpick.common.exception.DuplicateResourceException;
import com.toonpick.common.exception.EntityNotFoundException;
import com.toonpick.common.type.ErrorCode;
import com.toonpick.domain.webtoon.entity.Platform;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.entity.WebtoonPlatform;
import com.toonpick.domain.webtoon.entity.WebtoonStatistics;
import com.toonpick.domain.webtoon.repository.PlatformRepository;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.worker.dto.command.EpisodeRequest;
import com.toonpick.worker.dto.command.WebtoonCreateCommend;
import com.toonpick.worker.mapper.WebtoonMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 웹툰 등록 서비스
 * 웹툰 등록과 관련된 비즈니스 로직을 처리하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WebtoonRegistrationService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonMapper webtoonMapper;
    private final PlatformRepository platformRepository;
    private final WebtoonEpisodeUpdateService webtoonEpisodeUpdateService;
    private final WebtoonDuplicateCheckService duplicateCheckService;

    /**
     * 새로운 웹툰을 등록합니다.
     * 중복 체크 후 적절한 처리(신규 등록 또는 플랫폼 추가)를 수행합니다.
     */
    @Transactional
    public void createWebtoon(WebtoonCreateCommend request) {
        // 중복 체크 수행
        Optional<Webtoon> existingWebtoonOpt = duplicateCheckService.findDuplicateWebtoon(request);

        if (existingWebtoonOpt.isPresent()) {
            handleDuplicateWebtoon(existingWebtoonOpt.get(), request);
        } else {
            registerNewWebtoon(request);
        }
    }

    /**
     * 중복된 웹툰 처리
     */
    private void handleDuplicateWebtoon(Webtoon existingWebtoon, WebtoonCreateCommend request) {
        if (duplicateCheckService.hasPlatform(existingWebtoon, request.getPlatform())) {

            throw new DuplicateResourceException("이미 등록된 웹툰입니다: " + request.getTitle() + " " + request.getPlatform());
        } else {
            addPlatform(existingWebtoon, request);
        }
    }

    /**
     * 완전히 새로운 웹툰 등록
     */
    private void registerNewWebtoon(WebtoonCreateCommend request) {
        Webtoon webtoon = webtoonMapper.toWebtoon(request);
        WebtoonStatistics statistics = new WebtoonStatistics(webtoon);
        statistics.setEpisodeCount(request.getEpisodeCount() != null ? request.getEpisodeCount() : 0);

        Webtoon saved = webtoonRepository.save(webtoon);

        addPlatform(webtoon, request);
        
        // 에피소드 등록
        if (request.getEpisodes() != null && !request.getEpisodes().isEmpty()) {
            registerEpisodes(webtoon, request);
        }
    }

    /**
     * 기존 웹툰에 새로운 플랫폼 추가
     */
    private void addPlatform(Webtoon webtoon, WebtoonCreateCommend request) {
        Platform platform = platformRepository.findByName(request.getPlatform())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PLATFORM_NOT_FOUND));

        WebtoonPlatform webtoonPlatform = WebtoonPlatform.builder()
                .link(request.getUrl())
                .platform(platform)
                .webtoon(webtoon)
                .build();

        webtoon.getPlatforms().add(webtoonPlatform);
    }

    /**
     * 에피소드 등록
     */
    private void registerEpisodes(Webtoon webtoon, WebtoonCreateCommend request) {
        for (EpisodeRequest episodeRequest : request.getEpisodes()) {
            webtoonEpisodeUpdateService.createNewEpisode(webtoon, request.getPlatform(), episodeRequest);
        }
    }
}
