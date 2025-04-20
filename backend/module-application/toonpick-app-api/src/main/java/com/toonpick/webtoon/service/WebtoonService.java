package com.toonpick.webtoon.service;


import com.toonpick.dto.PagedResponseDTO;
import com.toonpick.dto.WebtoonFilterDTO;
import com.toonpick.entity.Author;
import com.toonpick.entity.Genre;
import com.toonpick.entity.Webtoon;
import com.toonpick.exception.ResourceNotFoundException;
import com.toonpick.repository.AuthorRepository;
import com.toonpick.repository.GenreRepository;
import com.toonpick.repository.WebtoonAnalysisRepository;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.type.ErrorCode;
import com.toonpick.webtoon.mapper.WebtoonAnalysisMapper;
import com.toonpick.webtoon.mapper.WebtoonMapper;
import com.toonpick.webtoon.request.WebtoonRequestDTO;
import com.toonpick.webtoon.response.WebtoonDetailsResponse;
import com.toonpick.webtoon.response.WebtoonResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final WebtoonMapper webtoonMapper;
    private final WebtoonAnalysisRepository analysisRepository;
    private final WebtoonAnalysisMapper analysisMapper;

    private static final Logger logger = LoggerFactory.getLogger(WebtoonService.class);

    /**
     * id 기반으로 웹툰 조회
     */
    @Transactional(readOnly = true)
    public WebtoonResponse getWebtoon(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, id));
        return webtoonMapper.toWebtoonResponse(webtoon);
    }

    /**
     * id 기반으로 웹툰 상세 데이터 조회
     */
    public WebtoonDetailsResponse getWebtoonDetails(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("웹툰이 존재하지 않습니다."));

        // 기본 정보 매핑
        WebtoonDetailsResponse webtoonDetails = webtoonMapper.toWebtoonDetailsResponse(webtoon);

        // 분석 데이터 조회 및 매핑
        analysisRepository.findByWebtoonId(id)
                .map(analysisMapper::toDto)
                .ifPresent(webtoonDetails::setAnalysisData);

        // todo : 유사 웹툰 (옵션) 추가 예정

        return webtoonDetails;
    }

    /**
     * Filter 옵션 및 page 에 맞춰 Webtoon 리스트 가져오기
     */
    @Transactional(readOnly = true)
    public PagedResponseDTO<WebtoonResponse> getWebtoonsOptions(
            WebtoonFilterDTO filter, int page, int size, String sortBy, String sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<Webtoon> webtoonPage = webtoonRepository.findWebtoonsByFilterOptions(filter, pageable);

        List<WebtoonResponse> webtoonDTOs = webtoonPage.getContent().stream()
                .map(webtoonMapper::toWebtoonResponse)
                .collect(Collectors.toList());

        return PagedResponseDTO.<WebtoonResponse>builder()
                .data(webtoonDTOs)
                .page(webtoonPage.getNumber())
                .size(webtoonPage.getSize())
                .totalElements(webtoonPage.getTotalElements())
                .totalPages(webtoonPage.getTotalPages())
                .last(webtoonPage.isLast())
                .build();
    }

    /**
     * 기존 웹툰 업데이트
     */
    @Transactional
    public WebtoonResponse updateWebtoon(Long id, WebtoonRequestDTO webtoonRequestDTO) {
        Webtoon existingWebtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, id));

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(webtoonRequestDTO.getAuthorIds()));
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(webtoonRequestDTO.getGenreIds()));

        existingWebtoon.updateWebtoonDetails(
                webtoonRequestDTO.getTitle(),
                webtoonRequestDTO.getPlatform(),
                webtoonRequestDTO.getDescription(),
                webtoonRequestDTO.getSerializationStatus(),
                webtoonRequestDTO.getDayOfWeek(),
                webtoonRequestDTO.getThumbnailUrl(),
                webtoonRequestDTO.getLink(),
                webtoonRequestDTO.getAgeRating(),
                authors,
                genres
        );

        existingWebtoon = webtoonRepository.save(existingWebtoon);

        return webtoonMapper.toWebtoonResponse(existingWebtoon);
    }


    /**
     * 웹툰 삭제
     */
    @Transactional
    public void deleteWebtoon(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, id));
        webtoonRepository.delete(webtoon);
    }

}
