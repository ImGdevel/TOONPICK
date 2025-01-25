package toonpick.app.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.dto.webtoon.AuthorDTO;
import toonpick.app.dto.webtoon.GenreDTO;
import toonpick.app.dto.PagedResponseDTO;
import toonpick.app.dto.WebtoonFilterDTO;
import toonpick.app.domain.webtoon.Author;
import toonpick.app.domain.webtoon.Genre;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.dto.webtoon.WebtoonCreateRequestDTO;
import toonpick.app.dto.webtoon.WebtoonEpisodeUpdateRequestDTO;
import toonpick.app.dto.webtoon.WebtoonRequestDTO;
import toonpick.app.dto.webtoon.WebtoonResponseDTO;
import toonpick.app.exception.ErrorCode;
import toonpick.app.exception.exception.ResourceAlreadyExistsException;
import toonpick.app.exception.exception.ResourceNotFoundException;
import toonpick.app.mapper.WebtoonMapper;
import toonpick.app.repository.AuthorRepository;
import toonpick.app.repository.GenreRepository;
import toonpick.app.repository.WebtoonRepository;

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

    private static final Logger logger = LoggerFactory.getLogger(WebtoonService.class);

    // 웹툰 추가
    @Transactional
    public WebtoonResponseDTO createWebtoon(WebtoonCreateRequestDTO createRequestDTO) {
        if (webtoonRepository.findByExternalId(createRequestDTO.getExternalId()).isPresent()) {
            throw new ResourceAlreadyExistsException(ErrorCode.WEBTOON_ALREADY_EXISTS, createRequestDTO.getTitle());
        }

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(
                createRequestDTO.getAuthors().stream().map(AuthorDTO::getId).collect(Collectors.toSet())));

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(
                createRequestDTO.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toSet())));

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

    // 간단한 정기적 업데이트
    @Transactional
    public void updateWebtoonEpisode(Long id, WebtoonEpisodeUpdateRequestDTO updateRequest) {
        Webtoon existingWebtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, id));

        existingWebtoon.updateEpisodeCountAndDate(
                updateRequest.getEpisodeCount(),
                updateRequest.getLastUpdatedDate(),
                updateRequest.getLastUpdatedDate()
        );

        webtoonRepository.save(existingWebtoon);
    }

    // 웹툰 삭제
    @Transactional
    public void deleteWebtoon(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, id));
        webtoonRepository.delete(webtoon);
    }

}
