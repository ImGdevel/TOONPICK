package toonpick.app.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.AuthenticationException;

import toonpick.app.security.dto.LoginRequest;
import toonpick.app.security.handler.LoginFailureHandler;
import toonpick.app.security.handler.LoginSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final LoginSuccessHandler successHandler;
    private final LoginFailureHandler failureHandler;

    private static final Logger logger = LoggerFactory.getLogger(LoginAuthenticationFilter.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            String requestBody = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            LoginRequest loginRequest = objectMapper.readValue(requestBody, LoginRequest.class);

            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            logger.error("Failed to parse authentication request body", e);
            throw new AuthenticationServiceException("Failed to parse authentication request body", e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        // 로그인 성공
        successHandler.onAuthenticationSuccess(request, response, authentication);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        // 로그인 실패
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

}
