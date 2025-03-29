package toonpick.app.service;

import lombok.RequiredArgsConstructor;
import toonpick.app.dto.webtoon.GenreDTO;
import toonpick.app.domain.webtoon.Genre;
import toonpick.type.ErrorCode;
import toonpick.exception.ResourceNotFoundException;
import toonpick.app.mapper.GenreMapper;
import toonpick.app.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Transactional(readOnly = true)
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(genreMapper::genreToGenreDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GenreDTO getGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GENRE_NOT_FOUND, id));
        return genreMapper.genreToGenreDto(genre);
    }

    @Transactional
    public Genre findOrCreateGenreEntity(String genreName) {
        return genreRepository.findByName(genreName)
                .orElseGet(() -> genreRepository.save(Genre.builder().name(genreName).build()));
    }

    @Transactional
    public GenreDTO findOrCreateGenreDTO(String genreName) {
        Genre genre = findOrCreateGenreEntity(genreName);
        return genreMapper.genreToGenreDto(genre);
    }

    @Transactional
    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GENRE_NOT_FOUND, id));
        genreRepository.delete(genre);
    }
}
