package com.toonpick.app.config;


import com.toonpick.app.dto.AuthorDTO;
import com.toonpick.app.dto.GenreDTO;
import com.toonpick.app.service.AuthorService;
import com.toonpick.app.service.GenreService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private final AuthorService authorService;
    private final GenreService genreService;

    public DataInitializer(AuthorService authorService, GenreService genreService) {
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @Bean
    CommandLineRunner initDatabase() {

        return args -> {
            // Author 초기 데이터
//            authorService.createAuthor(AuthorDTO.builder().name("Author One").build());
//            authorService.createAuthor(AuthorDTO.builder().name("Author Two").build());
//            authorService.createAuthor(AuthorDTO.builder().name("Author Three").build());
//
//            // Genre 초기 데이터
//            genreService.createGenre(GenreDTO.builder().name("Fantasy").build());
//            genreService.createGenre(GenreDTO.builder().name("Science Fiction").build());
//            genreService.createGenre(GenreDTO.builder().name("Mystery").build());
        };
    }
}