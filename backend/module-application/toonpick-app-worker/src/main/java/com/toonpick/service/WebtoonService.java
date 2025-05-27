package com.toonpick.service;


import com.toonpick.dto.result.WebtoonCreateResult;
import com.toonpick.dto.result.WebtoonEpisodeUpdateResult;
import com.toonpick.entity.Author;
import com.toonpick.entity.Webtoon;
import com.toonpick.entity.WebtoonStatistics;
import com.toonpick.exception.DuplicateResourceException;
import com.toonpick.mapper.WebtoonMapper;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;

    private final WebtoonMapper webtoonMapper;

    /**
     * 웹툰이 존재하지 않는 경우에만 새로 생성
     */
    public void createWebtoon(WebtoonCreateResult request) {
        // 중복 웹툰 처리
        if (handleIfDuplicateWebtoon(request).isPresent()) return;

        // 신규 웹툰 등록
        Webtoon webtoon = webtoonMapper.toWebtoon(request);
        WebtoonStatistics webtoonStatistics = new WebtoonStatistics(webtoon);
        webtoonStatistics.setEpisodeCount(request.getEpisodeCount());

        webtoonRepository.save(webtoon);
    }

    /**
     * 기존 웹툰과 변경사항이 있는 경우에만 업데이트
     */
    public boolean updateWebtoon(WebtoonEpisodeUpdateResult request) {
        Webtoon webtoon = webtoonRepository.findById(request.getId())
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.WEBTOON_NOT_FOUND.getMessage()));

        webtoon.getLastUpdatedDate();

        // todo : 추가 업데이트 로직 작성

        webtoonRepository.save(webtoon);
        return true;
    }

    /**
     * 신규 에피소드가 나왔다면 업데이트
     */
    public boolean updateWebtoonEpisode(WebtoonEpisodeUpdateResult request) {
        Webtoon webtoon = webtoonRepository.findById(request.getId())
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.WEBTOON_NOT_FOUND.getMessage()));

        webtoon.getLastUpdatedDate();

        // todo : 추가 업데이트 로직 작성

        webtoonRepository.save(webtoon);
        return true;
    }

    /**
     * 등록을 시도하는 웹툰이 이미 동록된 웹툰인지 판별
     */
    private Optional<Webtoon> handleIfDuplicateWebtoon(WebtoonCreateResult request) {
        List<Webtoon> sameTitleWebtoons = webtoonRepository.findAllByTitle(request.getTitle());

        for (Webtoon existing : sameTitleWebtoons) {

            // 웹툰 제목 + 작가 전부 같다면 같은 웹툰으로 판별
            if (hasSameAuthors(existing.getAuthors(), request.getAuthors())) {
                boolean alreadyExists = existing.getPlatforms().stream()
                    .anyMatch(p -> p.getPlatform().getName().equals(request.getPlatform().name()));

                if (alreadyExists) {
                    throw new DuplicateResourceException(ErrorCode.WEBTOON_ALREADY_EXISTS);
                }
                return Optional.of(existing);
            }
        }
        return Optional.empty();
    }

    /**
     * 동일한 작가가 있는지 체크
     */
    private boolean hasSameAuthors(Set<Author> existingAuthors, Set<WebtoonCreateResult.AuthorRequest> incomingRequests) {
        Set<String> existingNames = existingAuthors.stream()
                .map(Author::getName)
                .collect(Collectors.toSet());

        Set<String> incomingNames = incomingRequests.stream()
                .map(WebtoonCreateResult.AuthorRequest::getName)
                .collect(Collectors.toSet());

        return existingNames.equals(incomingNames);
    }
}
