package com.toonpick.test.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Profile("test")
@TestConfiguration
public class TestWebMvcConfig implements WebMvcConfigurer {

    private final HandlerMethodArgumentResolver testCurrentUserArgumentResolver;

    public TestWebMvcConfig(HandlerMethodArgumentResolver testCurrentUserArgumentResolver) {
        this.testCurrentUserArgumentResolver = testCurrentUserArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(testCurrentUserArgumentResolver);
    }
} 