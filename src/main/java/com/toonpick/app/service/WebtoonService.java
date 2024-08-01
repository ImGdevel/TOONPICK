package com.toonpick.app.service;

import com.toonpick.app.dto.WebtoonDTO;
import com.toonpick.app.entity.Webtoon;
import com.toonpick.app.exception.ResourceNotFoundException;
import com.toonpick.app.mapper.WebtoonMapper;
import com.toonpick.app.repository.WebtoonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.toonpick.app.entity.Author;
import com.toonpick.app.entity.Genre;
import com.toonpick.app.repository.AuthorRepository;
import com.toonpick.app.repository.GenreRepository;


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
        Webtoon webtoon = webtoonMapper.toEntity(webtoonDTO);
        webtoon = webtoonRepository.save(webtoon);
        return webtoonMapper.toDTO(webtoon);
    }

    @Transactional
    public WebtoonDTO updateWebtoon(Long id, WebtoonDTO webtoonDTO) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found with id: " + id));

        Author author = authorRepository.findById(webtoonDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + webtoonDTO.getAuthorId()));

        Set<Genre> genres = webtoonDTO.getGenreIds().stream()
                .map(genreId -> genreRepository.findById(genreId)
                        .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + genreId)))
                .collect(Collectors.toSet());

        webtoon.update(
                webtoonDTO.getTitle(),
                author,
                webtoonDTO.getRating(),
                webtoonDTO.getDescription(),
                webtoonDTO.getImageUrl(),
                genres
        );

        webtoon = webtoonRepository.save(webtoon);
        return webtoonMapper.toDTO(webtoon);
    }

    @Transactional(readOnly = true)
    public WebtoonDTO getWebtoon(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found with id: " + id));
        return webtoonMapper.toDTO(webtoon);
    }

    @Transactional(readOnly = true)
    public List<WebtoonDTO> getAllWebtoons() {
        List<Webtoon> webtoons = webtoonRepository.findAll();
        return webtoons.stream()
                .map(webtoonMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteWebtoon(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found with id: " + id));
        webtoonRepository.delete(webtoon);
    }
}
