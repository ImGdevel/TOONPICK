package com.toonpick.app.service;

import com.toonpick.app.dto.AuthorDTO;
import com.toonpick.app.dto.GenreDTO;
import com.toonpick.app.dto.WebtoonDTO;
import com.toonpick.app.entity.Webtoon;
import com.toonpick.app.exception.ResourceNotFoundException;
import com.toonpick.app.mapper.WebtoonMapper;
import com.toonpick.app.repository.WebtoonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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
        // DTO에서 엔티티로 변환
        Webtoon webtoon = webtoonMapper.webtoonDtoToWebtoon(webtoonDTO);

        // 연관된 엔티티 설정
        Set<Author> authors = new HashSet<>(authorRepository.findAllById(
                webtoonDTO.getAuthors().stream().map(AuthorDTO::getId).collect(Collectors.toSet())));

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(
                webtoonDTO.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toSet())));

        // 엔티티 상태 업데이트
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

        // 연관된 엔티티 설정
        Set<Author> authors = new HashSet<>(authorRepository.findAllById(
                webtoonDTO.getAuthors().stream().map(AuthorDTO::getId).collect(Collectors.toSet())));

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(
                webtoonDTO.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toSet())));

        // 엔티티 상태 업데이트
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

        // 엔티티를 저장하고 다시 DTO로 변환
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

    @Transactional
    public void deleteWebtoon(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found with id: " + id));
        webtoonRepository.delete(webtoon);
    }
}