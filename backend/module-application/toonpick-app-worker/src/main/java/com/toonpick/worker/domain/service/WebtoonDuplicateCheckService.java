package com.toonpick.worker.domain.service;

import com.toonpick.domain.webtoon.entity.Author;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.worker.dto.command.AuthorRequest;
import com.toonpick.worker.dto.command.WebtoonCreateCommend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 웹툰 중복 체크 서비스
 * 웹툰의 중복 여부를 판단하는 전용 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebtoonDuplicateCheckService {

    private final WebtoonRepository webtoonRepository;

    /**
     * 웹툰 중복 여부를 확인하고 기존 웹툰을 반환합니다.
     * 
     * @param request 웹툰 생성 요청
     * @return 기존 웹툰 (중복인 경우), 빈 Optional (중복이 아닌 경우)
     */
    public Optional<Webtoon> findDuplicateWebtoon(WebtoonCreateCommend request) {
        // 작가 정보 추출
        List<String> authorNames = extractAuthorNames(request.getAuthors());
        int authorCount = authorNames.size();
        
        if (authorCount == 0) {
            // 작가 정보가 없는 경우 기존 방식 사용
            return findDuplicateByTitleOnly(request);
        }
        
        // 최적화된 쿼리 사용
        return webtoonRepository.findExactMatchByTitleAndAuthors(
            request.getTitle(), authorNames, authorCount);
    }

    /**
     * 특정 플랫폼에서 이미 등록된 웹툰인지 확인합니다.
     * 
     * @param webtoon 웹툰
     * @param platformName 플랫폼명
     * @return 해당 플랫폼에 등록된 웹툰 여부
     */
    public boolean hasPlatform(Webtoon webtoon, String platformName) {
        return webtoon.getPlatforms().stream()
                .anyMatch(platform -> platform.getPlatform().getName().equals(platformName));
    }

    /**
     * 작가 정보가 없는 경우 제목만으로 중복 체크
     */
    private Optional<Webtoon> findDuplicateByTitleOnly(WebtoonCreateCommend request) {
        List<Webtoon> candidates = webtoonRepository.findAllByTitle(request.getTitle());
        
        if (candidates.isEmpty()) {
            return Optional.empty();
        }

        // 작가 정보가 없는 경우 첫 번째 웹툰을 중복으로 간주
        // (실제로는 더 정교한 로직이 필요할 수 있음)
        return Optional.of(candidates.get(0));
    }

    /**
     * 작가 이름 목록을 추출합니다.
     */
    private List<String> extractAuthorNames(List<AuthorRequest> authors) {
        if (authors == null || authors.isEmpty()) {
            return List.of();
        }
        
        return authors.stream()
                .map(AuthorRequest::getName)
                .filter(name -> name != null && !name.trim().isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * 동일한 작가인지 비교합니다.
     * 
     * @param existing 기존 작가들
     * @param incoming 새로운 작가들
     * @return 동일한 작가 여부
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
} 