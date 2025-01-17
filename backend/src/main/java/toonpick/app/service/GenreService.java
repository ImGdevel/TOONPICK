package toonpick.app.service;

import lombok.RequiredArgsConstructor;
import toonpick.app.dto.GenreDTO;
import toonpick.app.domain.webtoon.Genre;
import toonpick.app.exception.ErrorCode;
import toonpick.app.exception.ResourceAlreadyExistsException;
import toonpick.app.exception.ResourceNotFoundException;
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
    public GenreDTO createGenre(GenreDTO genreDTO) {
        if (genreRepository.existsByName(genreDTO.getName())) {
            throw new ResourceAlreadyExistsException(ErrorCode.GENRE_ALREADY_EXISTS, genreDTO.getName());
        }

        Genre genre = genreMapper.genreDtoToGenre(genreDTO);
        genre = genreRepository.save(genre);
        return genreMapper.genreToGenreDto(genre);
    }

    @Transactional
    public GenreDTO findOrCreateGenre(GenreDTO genreDTO){
        return genreRepository.findByName(genreDTO.getName())
                .map(genreMapper::genreToGenreDto)
                .orElseGet(() -> {
                    Genre genre = genreMapper.genreDtoToGenre(genreDTO);
                    genre = genreRepository.save(genre);
                    return genreMapper.genreToGenreDto(genre);
                });
    }

    @Transactional
    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GENRE_NOT_FOUND, id));
        genreRepository.delete(genre);
    }
}
