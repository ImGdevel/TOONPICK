package config;

import com.toonpick.service.AuthorService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
class TestConfig {
    @Bean
    public AuthorService authorService() {
        return Mockito.mock(AuthorService.class);
    }
}
