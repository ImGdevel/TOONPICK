package com.toonpick.webtoon.service;


import com.toonpick.domain.dto.PagedResponseDTO;
import com.toonpick.domain.webtoon.dto.WebtoonFilterDTO;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.common.exception.EntityNotFoundException;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.common.type.ErrorCode;
import com.toonpick.webtoon.mapper.WebtoonMapper;
import com.toonpick.webtoon.response.WebtoonDetailsResponse;
import com.toonpick.webtoon.response.WebtoonResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonMapper webtoonMapper;

    private static final Logger logger = LoggerFactory.getLogger(WebtoonService.class);

    /**
     * id 기반으로 웹툰 조회
     */
    @Transactional(readOnly = true)
    public WebtoonResponse getWebtoon(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, String.valueOf(id)));
        return webtoonMapper.toWebtoonResponse(webtoon);
    }

    /**
     * id 기반으로 웹툰 상세 데이터 조회
     */
    public WebtoonDetailsResponse getWebtoonDetails(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, String.valueOf(id)));

        // 기본 정보 매핑
        WebtoonDetailsResponse webtoonDetails = webtoonMapper.toWebtoonDetailsResponse(webtoon);

        // 분석 데이터 조회 및 매핑

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

}
