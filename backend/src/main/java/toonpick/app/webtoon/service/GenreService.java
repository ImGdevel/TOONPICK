package toonpick.app.webtoon.service;

import toonpick.app.webtoon.dto.GenreDTO;
import toonpick.app.webtoon.entity.Genre;
import toonpick.app.common.exception.ResourceNotFoundException;
import toonpick.app.webtoon.mapper.GenreMapper;
import toonpick.app.webtoon.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreService(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @Transactional(readOnly = true)
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(genreMapper::genreToGenreDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GenreDTO getGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));
        return genreMapper.genreToGenreDto(genre);
    }



    @Transactional
    public GenreDTO createGenre(GenreDTO genreDTO) {
        if (genreRepository.existsByName(genreDTO.getName())) {
            throw new IllegalArgumentException("Genre with name " + genreDTO.getName() + " already exists.");
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
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));
        genreRepository.delete(genre);
    }
}
