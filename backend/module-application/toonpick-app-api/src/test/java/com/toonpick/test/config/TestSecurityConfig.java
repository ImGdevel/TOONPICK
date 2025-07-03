package com.toonpick.test.config;

import com.toonpick.internal.security.annotation.CurrentUser;
import com.toonpick.internal.security.dto.CustomUserDetails;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@EnableWebSecurity
@Profile("test")
@TestConfiguration
public class TestSecurityConfig {
    
    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .oauth2Login(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public HandlerMethodArgumentResolver testCurrentUserArgumentResolver() {
        return new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.hasParameterAnnotation(CurrentUser.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                // SecurityContextHolder에서 실제 인증 정보를 가져옴
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                
                if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
                    // 실제 인증된 사용자가 있는 경우
                    return CustomUserDetails.builder()
                            .username(authentication.getName())
                            .role("ROLE_USER")
                            .build();
                } else {
                    // 인증되지 않은 경우 기본 테스트 사용자 반환
                    return CustomUserDetails.builder()
                            .username("testuser")
                            .role("ROLE_USER")
                            .build();
                }
            }
        };
    }

}