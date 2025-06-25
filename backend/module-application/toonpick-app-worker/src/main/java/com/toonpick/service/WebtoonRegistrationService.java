package com.toonpick.service;


import com.toonpick.dto.command.AuthorRequest;
import com.toonpick.dto.command.EpisodeRequest;
import com.toonpick.dto.command.WebtoonCreateCommend;
import com.toonpick.entity.*;
import com.toonpick.exception.DuplicateResourceException;
import com.toonpick.exception.EntityNotFoundException;
import com.toonpick.mapper.WebtoonMapper;
import com.toonpick.repository.PlatformRepository;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WebtoonRegistrationService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonMapper webtoonMapper;
    private final PlatformRepository platformRepository;
    private final WebtoonEpisodeUpdateService webtoonEpisodeUpdateService;

    /**
     *  새로운 웹툰을 등록합니다.
     */
    public void createWebtoon(WebtoonCreateCommend request) {
        // 동일한 웹툰(제목+작가) 찾기
        //  *  웹툰이 동일하다고 판단을 내리는 기준은 다음과 같다. > 웹툰 제목이 동일하면서 연재 작가 이름이 동일한 경우
        Optional<Webtoon> existingWebtoonOpt = findExistingWebtoon(request);

        if (existingWebtoonOpt.isPresent()) {
            // 만약 이미 존재하는 웹툰이라면 플랫폼 등록
            //  *  다음과 같은 경우에는 동일한 웹툰이지만 플랫품이 다른 경우 추가 등록해야한다. > 따라서 플랫폼 이 동일한지 체크하고 동맇하지 않다면 추가 플랫폼을 등록한다.
            Webtoon existingWebtoon = existingWebtoonOpt.get();

            if (hasPlatform(existingWebtoon, request.getPlatform())) {
                throw new DuplicateResourceException(ErrorCode.WEBTOON_ALREADY_EXISTS);
            }

            addPlatform(existingWebtoon, request);
            return;
        }
        
        // 완전히 새로운 웹툰 등록
        registerNewWebtoon(request);
    }

    /**
     * 완전히 새로운 웹툰 등록
     */
    private void registerNewWebtoon(WebtoonCreateCommend request) {
        log.info("웹툰 등록을 시도합니다. : {}", request.toString() );

        Webtoon webtoon = webtoonMapper.toWebtoon(request);
        WebtoonStatistics statistics = new WebtoonStatistics(webtoon);
        statistics.setEpisodeCount(request.getEpisodeCount() != null ? request.getEpisodeCount() : 0);

        Webtoon saved =  webtoonRepository.save(webtoon);

        addPlatform(webtoon, request);
        
        // 에피소드 등록
        log.info("에피소드를 등록합니다. {}", request.getEpisodes().size());
        if (request.getEpisodes() != null && !request.getEpisodes().isEmpty()) {
            registerEpisodes(webtoon, request);
        }
        log.info("웹툰 등록을 완료합니다. {}", saved.toString());
    }

    /**
     * 기존 웹툰에 새로운 플랫폼 추가
     */
    private void addPlatform(Webtoon webtoon, WebtoonCreateCommend request) {
        Platform platform = platformRepository.findByName(request.getPlatform())
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.PLATFORM_NOT_FOUND));

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

    /// ///
    /// 같은 웹툰 찾기
    /// ///
    
    /**
     * 제목+작가가 동일한 기존 웹툰을 찾는다
     */
    private Optional<Webtoon> findExistingWebtoon(WebtoonCreateCommend request) {
        List<Webtoon> candidates = webtoonRepository.findAllByTitle(request.getTitle());

        for (Webtoon candidate : candidates) {
            if (isSameAuthors(candidate.getAuthors(), request.getAuthors())) {
                return Optional.of(candidate);
            }
        }

        return Optional.empty();
    }

    /**
     * 동일한 작가인지 비교
     */
    private boolean isSameAuthors(Set<Author> existing, List<AuthorRequest> incoming) {
        if (incoming == null || incoming.isEmpty()) {
            return existing.isEmpty();
        }

        Set<String> existingNames = existing.stream()
                .map(Author::getName)
                .collect(Collectors.toSet());

        Set<String> incomingNames = incoming.stream()
                .map(AuthorRequest::getName)
                .collect(Collectors.toSet());

        return existingNames.equals(incomingNames);
    }

    /**
     * 동일 플랫폼 존재 여부 확인
     */
    private boolean hasPlatform(Webtoon webtoon, String platformName) {
        for (WebtoonPlatform platform : webtoon.getPlatforms()) {
            if (platform.getPlatform().getName().equals(platformName)) {
                return true;
            }
        }
        return false;
    }
}
