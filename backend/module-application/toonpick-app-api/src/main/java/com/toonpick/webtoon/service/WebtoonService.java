package com.toonpick.webtoon.service;

import com.toonpick.entity.Webtoon;
import com.toonpick.exception.ResourceNotFoundException;
import com.toonpick.webtoon.mapper.WebtoonMapper;
import com.toonpick.repository.GenreRepository;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.toonpick.dto.PagedResponseDTO;
import com.toonpick.dto.WebtoonFilterDTO;
import com.toonpick.webtoon.request.WebtoonRequestDTO;
import com.toonpick.webtoon.response.WebtoonResponseDTO;
import com.toonpick.entity.Author;
import com.toonpick.entity.Genre;
import com.toonpick.repository.AuthorRepository;


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

    private final AuthorService authorService;
    private final GenreService genreService;

    private static final Logger logger = LoggerFactory.getLogger(WebtoonService.class);

    /**
     * id 기반으로 웹툰 조회
     */
    @Transactional(readOnly = true)
    public WebtoonResponseDTO getWebtoonById(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, id));
        return webtoonMapper.webtoonToWebtoonResponseDto(webtoon);
    }

    /**
     * Filter 옵션 및 page 에 맞춰 Webtoon 가져오기
     */
    @Transactional(readOnly = true)
    public PagedResponseDTO<WebtoonResponseDTO> getWebtoonsOptions(
            WebtoonFilterDTO filter, int page, int size, String sortBy, String sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<Webtoon> webtoonPage = webtoonRepository.findWebtoonsByFilterOptions(filter, pageable);

        List<WebtoonResponseDTO> webtoonDTOs = webtoonPage.getContent().stream()
                .map(webtoonMapper::webtoonToWebtoonResponseDto)
                .collect(Collectors.toList());

        return PagedResponseDTO.<WebtoonResponseDTO>builder()
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
    public WebtoonResponseDTO updateWebtoon(Long id, WebtoonRequestDTO webtoonRequestDTO) {
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

        return webtoonMapper.webtoonToWebtoonResponseDto(existingWebtoon);
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
