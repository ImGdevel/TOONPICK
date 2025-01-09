package toonpick.app.webtoon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.webtoon.dto.AuthorDTO;
import toonpick.app.webtoon.dto.GenreDTO;
import toonpick.app.webtoon.dto.WebtoonDTO;
import toonpick.app.webtoon.dto.WebtoonFilterDTO;
import toonpick.app.webtoon.dto.WebtoonUpdateRequestDTO;
import toonpick.app.webtoon.entity.Author;
import toonpick.app.webtoon.entity.Genre;
import toonpick.app.webtoon.entity.Webtoon;
import toonpick.app.webtoon.entity.enums.SerializationStatus;
import toonpick.app.common.exception.ResourceNotFoundException;
import toonpick.app.webtoon.mapper.WebtoonMapper;
import toonpick.app.webtoon.repository.AuthorRepository;
import toonpick.app.webtoon.repository.GenreRepository;
import toonpick.app.webtoon.repository.WebtoonRepository;

import java.time.DayOfWeek;
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

    // 특정 웹툰 가져오기
    @Transactional(readOnly = true)
    public WebtoonDTO getWebtoon(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found with id: " + id));
        return webtoonMapper.webtoonToWebtoonDto(webtoon);
    }

    // 모든 웹툰 가져오기 (안씀)
    @Transactional(readOnly = true)
    public List<WebtoonDTO> getAllWebtoons() {
        List<Webtoon> webtoons = webtoonRepository.findAll();
        return webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    // 특정 작가의 웹툰 가져오기
    @Transactional(readOnly = true)
    public List<WebtoonDTO> getWebtoonsByAuthorName(String authorName) {
        List<Webtoon> webtoons = webtoonRepository.findByAuthors_Name(authorName);
        return webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    //장르에 따라 웹툰 가져오기
    @Transactional(readOnly = true)
    public List<WebtoonDTO> getWebtoonsByGenreName(String genreName) {
        List<Webtoon> webtoons = webtoonRepository.findByGenres_Name(genreName);
        return webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }


    // 요일에 맞춘 웹툰 가져오기
    @Transactional(readOnly = true)
    public List<WebtoonDTO> getSeriesOfWebtoonsByDayOfWeek(DayOfWeek dayOfWeek) {
        List<SerializationStatus> statuses = List.of(
                SerializationStatus.ONGOING,
               SerializationStatus.PAUSED
        );

        List<Webtoon> webtoons = webtoonRepository.findWebtoonsByFilter(
                WebtoonFilterDTO.builder()
                        .week(dayOfWeek)
                        .serializationStatus(SerializationStatus.ONGOING)
                        .build()
        ).stream()
                .filter(w -> statuses.contains(w.getSerializationStatus()))
                .collect(Collectors.toList());

        return webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    // 웹툰 삭제
    @Transactional
    public void deleteWebtoon(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found with id: " + id));
        webtoonRepository.delete(webtoon);
    }

    // 연재 상황에 따른 웹툰 가져오기
    @Transactional(readOnly = true)
    public List<WebtoonDTO> getWebtoonsBySerializationStatus(SerializationStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "title"));
        Page<Webtoon> webtoonPage = webtoonRepository.findBySerializationStatus(status, pageable);

        return webtoonPage.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    // WebtoonFilterDTO에 맞춘 웹툰 가져오기
    @Transactional(readOnly = true)
    public List<WebtoonDTO> filterWebtoons(WebtoonFilterDTO filter) {
        List<Webtoon> webtoons = webtoonRepository.findWebtoonsByFilter(filter);
        return webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    // 필터 옵션에 따라 웹툰 가져오기
    @Transactional(readOnly = true)
    public List<WebtoonDTO> filterWebtoonsOptions(WebtoonFilterDTO filter, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<Webtoon> webtoonPage = webtoonRepository.findWebtoonsByFilterOptions(filter, pageable);
        return webtoonPage.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

}
