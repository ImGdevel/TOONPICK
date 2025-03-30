package com.toonpick.test.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import com.toonpick.service.AuthorService;


@TestConfiguration
class TestConfig {
    @Bean
    public AuthorService authorService() {
        return Mockito.mock(AuthorService.class);
    }
}
