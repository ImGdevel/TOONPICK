package toonpick.app.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import toonpick.app.dto.AuthorDTO;
import toonpick.app.dto.GenreDTO;
import toonpick.app.dto.JoinRequestDTO;
import toonpick.app.entity.User;
import toonpick.app.repository.UserRepository;
import toonpick.app.service.AuthorService;
import toonpick.app.service.GenreService;
import toonpick.app.service.JoinService;

@Configuration
public class DataInitializer {

    private final AuthorService authorService;
    private final GenreService genreService;
    private final JoinService joinService;


    public DataInitializer(AuthorService authorService, GenreService genreService, JoinService joinService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.joinService = joinService;
    }

    @Bean
    CommandLineRunner initDatabase() {

        return args -> {

            joinService.createUser(JoinRequestDTO.builder().username("admin").password("1234").build());


            // Author 초기 데이터
            authorService.createAuthor(AuthorDTO.builder().name("Author One").build());
            authorService.createAuthor(AuthorDTO.builder().name("Author Two").build());
            authorService.createAuthor(AuthorDTO.builder().name("Author Three").build());

            // Genre 초기 데이터
            genreService.createGenre(GenreDTO.builder().name("Fantasy").build());
            genreService.createGenre(GenreDTO.builder().name("Science Fiction").build());
            genreService.createGenre(GenreDTO.builder().name("Mystery").build());


        };
    }
}