package toonpick.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import toonpick.dto.PagedResponseDTO;
import toonpick.dto.WebtoonCreateRequestDTO;
import toonpick.dto.WebtoonFilterDTO;
import toonpick.dto.WebtoonRequestDTO;
import toonpick.dto.WebtoonResponseDTO;
import toonpick.entity.Author;
import toonpick.entity.Genre;
import toonpick.entity.Webtoon;
import toonpick.mapper.WebtoonMapper;
import toonpick.repository.AuthorRepository;
import toonpick.repository.GenreRepository;
import toonpick.repository.WebtoonRepository;
import toonpick.type.ErrorCode;
import toonpick.exception.ResourceAlreadyExistsException;
import toonpick.exception.ResourceNotFoundException;


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

    // 웹툰 추가
    @Transactional
    public WebtoonResponseDTO createWebtoon(WebtoonCreateRequestDTO createRequestDTO) {
        if (webtoonRepository.findByExternalId(createRequestDTO.getExternalId()).isPresent()) {
            throw new ResourceAlreadyExistsException(ErrorCode.WEBTOON_ALREADY_EXISTS, createRequestDTO.getTitle());
        }

        Set<Author> authors = createRequestDTO.getAuthors().stream()
                .map(authorService::findOrCreateAuthorEntity)
                .collect(Collectors.toSet());

        Set<Genre> genres = createRequestDTO.getGenres().stream()
                .map(genreService::findOrCreateGenreEntity)
                .collect(Collectors.toSet());

        Webtoon newWebtoon = Webtoon.builder()
                .externalId(createRequestDTO.getExternalId())
                .title(createRequestDTO.getTitle())
                .platform(createRequestDTO.getPlatform())
                .dayOfWeek(createRequestDTO.getDayOfWeek())
                .thumbnailUrl(createRequestDTO.getThumbnailUrl())
                .link(createRequestDTO.getLink())
                .ageRating(createRequestDTO.getAgeRating())
                .description(createRequestDTO.getDescription())
                .serializationStatus(createRequestDTO.getSerializationStatus())
                .episodeCount(createRequestDTO.getEpisodeCount())
                .platformRating(createRequestDTO.getPlatformRating())
                .publishStartDate(createRequestDTO.getPublishStartDate())
                .lastUpdatedDate(createRequestDTO.getLastUpdatedDate())
                .authors(authors)
                .genres(genres)
                .build();

        Webtoon savedWebtoon = webtoonRepository.save(newWebtoon);
        return webtoonMapper.webtoonToWebtoonResponseDto(savedWebtoon);
    }

    // Id 기반으로 웹툰 가져오기
    @Transactional(readOnly = true)
    public WebtoonResponseDTO getWebtoonById(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, id));
        return webtoonMapper.webtoonToWebtoonResponseDto(webtoon);
    }

    // 필터 옵션에 따라 웹툰 가져오기
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

    // 웹툰 세부 내용 업데이트
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


    // 웹툰 삭제
    @Transactional
    public void deleteWebtoon(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, id));
        webtoonRepository.delete(webtoon);
    }

}
