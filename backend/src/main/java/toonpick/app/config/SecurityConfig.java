package toonpick.app.config;

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
import toonpick.app.jwt.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import toonpick.app.jwt.JwtTokenProvider;
import toonpick.app.jwt.LoginAuthenticationFilter;
import toonpick.app.oauth2.CustomSuccessHandler;
import toonpick.app.service.AuthService;
import toonpick.app.service.OAuth2UserService;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2UserService oAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final AuthService authService;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration,
                          AuthService authService,
                          JwtTokenProvider jwtTokenProvider,
                          OAuth2UserService oAuth2UserService,
                          CustomSuccessHandler customSuccessHandler
                          ) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.oAuth2UserService = oAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
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
                        .successHandler(customSuccessHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/*").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/api/**").permitAll()
                        .requestMatchers("/", "/login", "/join", "/reissue").permitAll()
                        .requestMatchers("/hello").hasRole("USER")
                        .requestMatchers("/admin").hasRole("ADMIN")

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(new LoginAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtTokenProvider, authService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLogoutFilter(jwtTokenProvider, authService), LogoutFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthorizationFilter(jwtTokenProvider), OAuth2LoginAuthenticationFilter.class);

        return http.build();
    }

}
