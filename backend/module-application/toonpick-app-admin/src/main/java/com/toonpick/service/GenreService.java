package com.toonpick.service;


import com.toonpick.entity.Genre;
import com.toonpick.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;

    // todo : Genre 엔티티 원본을 반환하는 것이 맞을까? (WebtoonMapper 에서 사용되고 있다.)
    /**
     * Genre 등록 혹은 조회
     */
    public Genre findOrCreateGenre(String name) {
        return genreRepository.findByName(name)
                .orElseGet(() -> {
                    Genre newGenre = Genre.builder()
                            .name(name).build();
                    return genreRepository.save(newGenre);
                });
    }

}
