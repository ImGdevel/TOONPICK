package com.toonpick.config;

import com.toonpick.filter.CustomLogoutFilter;
import com.toonpick.filter.JwtAuthorizationFilter;
import com.toonpick.filter.LoginAuthenticationFilter;
import com.toonpick.handler.LoginFailureHandler;
import com.toonpick.handler.LoginSuccessHandler;
import com.toonpick.handler.LogoutHandler;
import com.toonpick.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.toonpick.utils.ErrorResponseSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.toonpick.jwt.JwtTokenValidator;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenValidator jwtTokenValidator;
    private final DefaultOAuth2UserService oAuth2UserService;
    private final SimpleUrlAuthenticationSuccessHandler oAuth2SuccessHandler;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final LogoutHandler logoutHandler;
    private final ErrorResponseSender errorResponseSender;
    private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;

    public SecurityConfig(
            AuthenticationConfiguration authenticationConfiguration,
            JwtTokenValidator jwtTokenValidator,
            DefaultOAuth2UserService oAuth2UserService,
            @Qualifier("OAuth2SuccessHandler") SimpleUrlAuthenticationSuccessHandler oAuth2SuccessHandler,
            @Qualifier("loginSuccessHandler") LoginSuccessHandler loginSuccessHandler,
            LoginFailureHandler loginFailureHandler,
            LogoutHandler logoutHandler,
            ErrorResponseSender errorResponseSender,
            HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository
    ) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtTokenValidator = jwtTokenValidator;
        this.oAuth2UserService = oAuth2UserService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
        this.logoutHandler = logoutHandler;
        this.errorResponseSender = errorResponseSender;
        this.authorizationRequestRepository = authorizationRequestRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(oAuth2UserService)
                )
                .authorizationEndpoint(authorization -> authorization
                    .authorizationRequestRepository(authorizationRequestRepository)
                )
                .successHandler(oAuth2SuccessHandler)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/join", "/logout", "/reissue", "/oauth2/*", "/api/public/*").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                .requestMatchers("/api/secure/*").hasRole("USER")
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterAt(
                new LoginAuthenticationFilter(authenticationManager(authenticationConfiguration), loginSuccessHandler, loginFailureHandler),
                UsernamePasswordAuthenticationFilter.class
            )
            .addFilterBefore(
                new CustomLogoutFilter(jwtTokenValidator, logoutHandler, errorResponseSender),
                LogoutFilter.class
            )
            .addFilterAfter(
                new JwtAuthorizationFilter(jwtTokenValidator, errorResponseSender),
                OAuth2LoginAuthenticationFilter.class
            );

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            configuration.setMaxAge(3600L);
            configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
            return configuration;
        };
    }


}
