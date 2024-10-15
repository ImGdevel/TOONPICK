package toonpick.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.dto.AuthorDTO;
import toonpick.app.dto.GenreDTO;
import toonpick.app.dto.WebtoonDTO;
import toonpick.app.dto.WebtoonFilterDTO;
import toonpick.app.entity.Author;
import toonpick.app.entity.Genre;
import toonpick.app.entity.Webtoon;
import toonpick.app.entity.enums.SerializationStatus;
import toonpick.app.exception.ResourceNotFoundException;
import toonpick.app.mapper.WebtoonMapper;
import toonpick.app.repository.AuthorRepository;
import toonpick.app.repository.GenreRepository;
import toonpick.app.repository.WebtoonRepository;

import java.time.DayOfWeek;
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

    @Transactional
    public WebtoonDTO createWebtoon(WebtoonDTO webtoonDTO) {
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

    @Transactional(readOnly = true)
    public WebtoonDTO getWebtoon(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found with id: " + id));
        return webtoonMapper.webtoonToWebtoonDto(webtoon);
    }

    @Transactional(readOnly = true)
    public List<WebtoonDTO> getAllWebtoons() {
        List<Webtoon> webtoons = webtoonRepository.findAll();
        return webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WebtoonDTO> getWebtoonsByAuthorName(String authorName) {
        List<Webtoon> webtoons = webtoonRepository.findByAuthors_Name(authorName);
        return webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WebtoonDTO> getWebtoonsByDayOfWeek(DayOfWeek dayOfWeek) {
        List<Webtoon> webtoons = webtoonRepository.findByWeek(dayOfWeek);
        return webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WebtoonDTO> getWebtoonsByGenreName(String genreName) {
        List<Webtoon> webtoons = webtoonRepository.findByGenres_Name(genreName);
        return webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WebtoonDTO> getWebtoonsBySerializationStatus(SerializationStatus status) {
        List<Webtoon> webtoons = webtoonRepository.findBySerializationStatus(status);
        return webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WebtoonDTO> getSeriesOfWebtoonsByDayOfWeek(DayOfWeek dayOfWeek) {
        List<SerializationStatus> statuses = List.of(
                SerializationStatus.연재,
               SerializationStatus.휴재
        );

        List<Webtoon> webtoons = webtoonRepository.findWebtoonsByFilter(
                WebtoonFilterDTO.builder()
                        .week(dayOfWeek)
                        .serializationStatus(SerializationStatus.연재)
                        .build()
        ).stream()
                .filter(w -> statuses.contains(w.getSerializationStatus()))
                .collect(Collectors.toList());

        return webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteWebtoon(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found with id: " + id));
        webtoonRepository.delete(webtoon);
    }

    @Transactional(readOnly = true)
    public List<WebtoonDTO> getWebtoonsBySerializationStatus(SerializationStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "title"));
        Page<Webtoon> webtoonPage = webtoonRepository.findBySerializationStatus(status, pageable);

        return webtoonPage.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    // 필터링 메서드 추가
    @Transactional(readOnly = true)
    public List<WebtoonDTO> filterWebtoons(WebtoonFilterDTO filter) {
        List<Webtoon> webtoons = webtoonRepository.findWebtoonsByFilter(filter);
        return webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WebtoonDTO> filterWebtoonsOptions(WebtoonFilterDTO filter, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<Webtoon> webtoonPage = webtoonRepository.findWebtoonsByFilterOptions(filter, pageable);
        return webtoonPage.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addRating(Long webtoonId, float newRating) {
        webtoonRepository.updateRating(webtoonId, newRating);
    }

}
