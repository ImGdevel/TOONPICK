package toonpick.app.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import toonpick.app.webtoon.service.AuthorService;

@TestConfiguration
class TestConfig {
    @Bean
    public AuthorService authorService() {
        return Mockito.mock(AuthorService.class);
    }
}
