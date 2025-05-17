package com.toonpick.service;

import com.toonpick.dto.request.AuthorRequest;
import com.toonpick.dto.request.WebtoonCreateRequest;
import com.toonpick.dto.request.WebtoonUpdateRequest;
import com.toonpick.entity.Author;
import com.toonpick.entity.Webtoon;
import com.toonpick.exception.DuplicateResourceException;
import com.toonpick.exception.EntityNotFoundException;
import com.toonpick.mapper.WebtoonMapper;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonMapper webtoonMapper;

    private final PlatformService platformService;

    /**
     * 새로운 웹툰 추가 (이미 존재하는 경우 거부)
     */
    public void createWebtoon(WebtoonCreateRequest request) {
        // 중복 웹툰 처리
        if (handleIfDuplicateWebtoon(request).isPresent()) return;

        // 신규 웹툰 등록
        Webtoon webtoon = webtoonMapper.toWebtoon(request);
        webtoonRepository.save(webtoon);
        platformService.registerWebtoonPlatform(webtoon, request.getPlatform().name(), request.getLink());
    }

    /**
     * 기존 웹툰 데이터 업데이트
     */
    public void updateWebtoon(WebtoonUpdateRequest webtoonUpdateRequest) {
        Webtoon webtoon = webtoonRepository.findById(webtoonUpdateRequest.getId())
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.WEBTOON_NOT_FOUND));

        // todo : 업데이트 가능한 데이터만 업데이트
        // webtoon.updateWebtoonDetails();

        webtoonRepository.save(webtoon);
    }

    /**
     * 웹툰 제거
     */
    public void deleteWebtoon(Long id) {
        if (!webtoonRepository.existsById(id)) {
            throw new EntityNotFoundException(ErrorCode.WEBTOON_NOT_FOUND);
        }
        webtoonRepository.deleteById(id);
    }

    /**
     * 등록을 시도하는 웹툰이 이미 동록된 웹툰인지 판별
     */
    private Optional<Webtoon> handleIfDuplicateWebtoon(WebtoonCreateRequest request) {
        List<Webtoon> sameTitleWebtoons = webtoonRepository.findAllByTitle(request.getTitle());

        for (Webtoon existing : sameTitleWebtoons) {

            // 웹툰 제목 + 작가 전부 같다면 같은 웹툰으로 판별
            if (hasSameAuthors(existing.getAuthors(), request.getAuthors())) {
                boolean alreadyExists = existing.getPlatforms().stream()
                    .anyMatch(p -> p.getPlatform().getName().equals(request.getPlatform().name()));

                if (alreadyExists) {
                    throw new DuplicateResourceException(ErrorCode.WEBTOON_ALREADY_EXISTS);
                }
                platformService.registerWebtoonPlatform(existing, request.getPlatform().name(), request.getLink());
                return Optional.of(existing);
            }
        }
        return Optional.empty();
    }

    /**
     * 동일한 작가가 있는지 체크
     */
    private boolean hasSameAuthors(Set<Author> existingAuthors, Set<AuthorRequest> incomingRequests) {
        Set<String> existingNames = existingAuthors.stream()
                .map(Author::getName)
                .collect(Collectors.toSet());

        Set<String> incomingNames = incomingRequests.stream()
                .map(AuthorRequest::getName)
                .collect(Collectors.toSet());

        return existingNames.equals(incomingNames);
    }
}
