package com.toonpick.service;

import com.toonpick.exception.ResourceNotFoundException;
import com.toonpick.mapper.GenreMapper;
import com.toonpick.repository.GenreRepository;
import com.toonpick.type.ErrorCode;
import lombok.RequiredArgsConstructor;

import com.toonpick.dto.GenreDTO;
import com.toonpick.entity.Genre;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    /**
     * id로 Genre 조회하기
     */
    @Transactional(readOnly = true)
    public GenreDTO getGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GENRE_NOT_FOUND, id));
        return genreMapper.genreToGenreDto(genre);
    }


    // todo : 아래 둘중 하나만 남기고 제거할 것
    /**
     * 장르명 조회하고 가져오기, 없다면 새로 생성
     * @return 장르 엔티티
     */
    @Transactional
    public Genre findOrCreateGenreEntity(String genreName) {
        return genreRepository.findByName(genreName)
                .orElseGet(() -> genreRepository.save(Genre.builder().name(genreName).build()));
    }

    /**
     * 장르명 조회하고 가져오기, 없다면 새로 생성
     * @return
     */
    @Transactional
    public GenreDTO findOrCreateGenreDTO(String genreName) {
        Genre genre = findOrCreateGenreEntity(genreName);
        return genreMapper.genreToGenreDto(genre);
    }

    /**
     * 특정 장르 제거
     */
    @Transactional
    public void deleteGenre(String genreName) {
        Genre genre = genreRepository.findByName(genreName)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GENRE_NOT_FOUND, genreName));
        genreRepository.delete(genre);
    }
}
