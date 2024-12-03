package toonpick.app.security.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import toonpick.app.security.handler.LoginFailureHandler;
import toonpick.app.security.handler.LoginSuccessHandler;
import toonpick.app.security.handler.LogoutHandler;
import toonpick.app.security.jwt.JwtTokenProvider;
import toonpick.app.security.filter.CustomLogoutFilter;
import toonpick.app.security.filter.JwtAuthorizationFilter;
import toonpick.app.security.filter.LoginAuthenticationFilter;
import toonpick.app.security.handler.OAuth2SuccessHandler;
import toonpick.app.security.jwt.JwtTokenValidator;
import toonpick.app.service.AuthService;
import toonpick.app.service.OAuth2UserService;
import toonpick.app.util.ErrorResponseSender;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler OAuth2SuccessHandler;
    private final AuthService authService;

    private final LoginSuccessHandler successHandler;
    private final LoginFailureHandler failureHandler;
    private final LogoutHandler logoutHandler;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration,
                          AuthService authService,
                          JwtTokenProvider jwtTokenProvider,
                          OAuth2UserService oAuth2UserService,
                          OAuth2SuccessHandler OAuth2SuccessHandler,
                          LoginSuccessHandler successHandler,
                          LoginFailureHandler failureHandler,
                          LogoutHandler logoutHandler
                          ) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.oAuth2UserService = oAuth2UserService;
        this.OAuth2SuccessHandler = OAuth2SuccessHandler;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.logoutHandler = logoutHandler;
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
    public ErrorResponseSender errorResponseSender() {
        return new ErrorResponseSender();
    }

    @Bean
    public JwtTokenValidator jwtTokenValidator() {
        return new JwtTokenValidator(jwtTokenProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ErrorResponseSender errorResponseSender, JwtTokenValidator jwtTokenValidator) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setMaxAge(3600L);
                        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
                        return configuration;
                    }
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .successHandler(OAuth2SuccessHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/*").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/api/**").permitAll()
                        .requestMatchers("/", "/login", "/join", "/reissue").permitAll()
                        .requestMatchers("/hello").hasRole("USER")
                        .requestMatchers("/admin").hasRole("ADMIN")

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(
                        new LoginAuthenticationFilter(authenticationManager(authenticationConfiguration), successHandler, failureHandler),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        new CustomLogoutFilter(jwtTokenValidator, logoutHandler),
                        LogoutFilter.class
                )
                .addFilterAfter(
                        new JwtAuthorizationFilter(jwtTokenValidator, errorResponseSender),
                        OAuth2LoginAuthenticationFilter.class
                );

        return http.build();
    }

}
