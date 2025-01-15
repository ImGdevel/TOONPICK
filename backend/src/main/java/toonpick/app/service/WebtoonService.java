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
import toonpick.app.dto.AuthorDTO;
import toonpick.app.dto.GenreDTO;
import toonpick.app.dto.PagedResponseDTO;
import toonpick.app.dto.WebtoonDTO;
import toonpick.app.dto.WebtoonFilterDTO;
import toonpick.app.dto.WebtoonUpdateRequestDTO;
import toonpick.app.domain.webtoon.Author;
import toonpick.app.domain.webtoon.Genre;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.exception.ResourceNotFoundException;
import toonpick.app.mapper.WebtoonMapper;
import toonpick.app.repository.AuthorRepository;
import toonpick.app.repository.GenreRepository;
import toonpick.app.repository.WebtoonRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    public WebtoonDTO createWebtoon(WebtoonDTO webtoonDTO) {
        Optional<Webtoon> existingWebtoon = webtoonRepository.findByPlatformId(webtoonDTO.getPlatformId());
        if (existingWebtoon.isPresent()) {
            return null;
        }

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(
                webtoonDTO.getAuthors().stream().map(AuthorDTO::getId).collect(Collectors.toSet())));

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(
                webtoonDTO.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toSet())));

        Webtoon webtoon = Webtoon.builder()
                .title(webtoonDTO.getTitle())
                .platform(webtoonDTO.getPlatform())
                .platformId(webtoonDTO.getPlatformId())
                .averageRating(webtoonDTO.getAverageRating())
                .description(webtoonDTO.getDescription())
                .episodeCount(webtoonDTO.getEpisodeCount())
                .serializationStartDate(webtoonDTO.getSerializationStartDate())
                .serializationStatus(webtoonDTO.getSerializationStatus())
                .week(webtoonDTO.getWeek())
                .thumbnailUrl(webtoonDTO.getThumbnailUrl())
                .url(webtoonDTO.getUrl())
                .ageRating(webtoonDTO.getAgeRating())
                .authors(authors)
                .genres(genres)
                .build();

        webtoon = webtoonRepository.save(webtoon);
        return webtoonMapper.webtoonToWebtoonDto(webtoon);
    }

    // Id 기반으로 웹툰 가져오기
    @Transactional(readOnly = true)
    public WebtoonDTO getWebtoonById(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found with id: " + id));
        return webtoonMapper.webtoonToWebtoonDto(webtoon);
    }

    // 필터 옵션에 따라 웹툰 가져오기
    @Transactional(readOnly = true)
    public PagedResponseDTO<WebtoonDTO> getWebtoonsOptions(
            WebtoonFilterDTO filter, int page, int size, String sortBy, String sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<Webtoon> webtoonPage = webtoonRepository.findWebtoonsByFilterOptions(filter, pageable);

        List<WebtoonDTO> webtoonDTOs = webtoonPage.getContent().stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());

        return PagedResponseDTO.<WebtoonDTO>builder()
                .data(webtoonDTOs)
                .page(webtoonPage.getNumber())
                .size(webtoonPage.getSize())
                .totalElements(webtoonPage.getTotalElements())
                .totalPages(webtoonPage.getTotalPages())
                .last(webtoonPage.isLast())
                .build();
    }

    // 웹툰 정보 업데이트
    @Transactional
    public WebtoonDTO updateWebtoon(Long id, WebtoonDTO webtoonDTO) {
        Webtoon existingWebtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found with id: " + id));

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(
                webtoonDTO.getAuthors().stream().map(AuthorDTO::getId).collect(Collectors.toSet())));

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(
                webtoonDTO.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toSet())));

        existingWebtoon.update(
                webtoonDTO.getTitle(),
                webtoonDTO.getPlatform(),
                webtoonDTO.getAverageRating(),
                webtoonDTO.getDescription(),
                webtoonDTO.getEpisodeCount(),
                webtoonDTO.getSerializationStartDate(),
                webtoonDTO.getLastUpdatedDate(),
                webtoonDTO.getSerializationStatus(),
                webtoonDTO.getWeek(),
                webtoonDTO.getThumbnailUrl(),
                webtoonDTO.getUrl(),
                webtoonDTO.getAgeRating(),
                authors,
                genres
        );

        existingWebtoon = webtoonRepository.save(existingWebtoon);
        return webtoonMapper.webtoonToWebtoonDto(existingWebtoon);
    }

    // 정기적 업데이트
    @Transactional
    public void updateWebtoon(WebtoonUpdateRequestDTO request) {
        Webtoon webtoon = webtoonRepository.findByPlatformId(request.getPlatformId())
                .orElseThrow(() -> new RuntimeException("Webtoon not found with platformId: " + request.getPlatformId()));

        Set<Author> authors = authorRepository.findByNameIn(request.getAuthors());
        Set<Genre> genres = genreRepository.findByNameIn(request.getGenres());

        webtoon.update(
                webtoon.getTitle(),
                webtoon.getPlatform(),
                request.getPlatformRating() != null ? request.getPlatformRating() : webtoon.getAverageRating(),
                request.getDescription(),
                request.getEpisodeCount() != null ? request.getEpisodeCount() : webtoon.getEpisodeCount(),
                webtoon.getSerializationStartDate(),
                request.getLastUpdatedDate() != null ? request.getLastUpdatedDate() : webtoon.getLastUpdatedDate(),
                request.getSerializationStatus(),
                request.getWeek(),
                request.getThumbnailUrl(),
                request.getUrl(),
                request.getAgeRating(),
                authors,
                genres
        );

        webtoonRepository.save(webtoon);
    }

    // 웹툰 삭제
    @Transactional
    public void deleteWebtoon(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found with id: " + id));
        webtoonRepository.delete(webtoon);
    }

}
