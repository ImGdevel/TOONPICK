package toonpick.app.service;

import toonpick.app.dto.AuthorDTO;
import toonpick.app.dto.GenreDTO;
import toonpick.app.dto.WebtoonDTO;
import toonpick.app.entity.Webtoon;
import toonpick.app.exception.ResourceNotFoundException;
import toonpick.app.mapper.WebtoonMapper;
import toonpick.app.repository.WebtoonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import toonpick.app.entity.Author;
import toonpick.app.entity.Genre;
import toonpick.app.repository.AuthorRepository;
import toonpick.app.repository.GenreRepository;

import java.util.Set;

@Service
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final WebtoonMapper webtoonMapper;

    public WebtoonService(WebtoonRepository webtoonRepository, AuthorRepository authorRepository, GenreRepository genreRepository, WebtoonMapper webtoonMapper) {
        this.webtoonRepository = webtoonRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.webtoonMapper = webtoonMapper;
    }

    @Transactional
    public WebtoonDTO createWebtoon(WebtoonDTO webtoonDTO) {
        Webtoon webtoon = webtoonMapper.webtoonDtoToWebtoon(webtoonDTO);

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(
                webtoonDTO.getAuthors().stream().map(AuthorDTO::getId).collect(Collectors.toSet())));

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(
                webtoonDTO.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toSet())));

        webtoon.update(
                webtoonDTO.getTitle(),
                webtoonDTO.getAverageRating(),
                webtoonDTO.getPlatformRating(),
                webtoonDTO.getDescription(),
                webtoonDTO.getEpisodeCount(),
                webtoonDTO.getSerializationStartDate(),
                webtoonDTO.getSerializationDay(),
                authors,
                genres
        );

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
                webtoonDTO.getAverageRating(),
                webtoonDTO.getPlatformRating(),
                webtoonDTO.getDescription(),
                webtoonDTO.getEpisodeCount(),
                webtoonDTO.getSerializationStartDate(),
                webtoonDTO.getSerializationDay(),
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
    public List<WebtoonDTO> getWebtoonsByGenreName(String genreName) {
        List<Webtoon> webtoons = webtoonRepository.findByGenres_Name(genreName);
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
}
