package com.toonpick.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.exception.CustomAuthenticationException;
import com.toonpick.handler.LoginSuccessHandler;
import com.toonpick.type.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.AuthenticationException;

import com.toonpick.dto.LoginRequest;
import com.toonpick.handler.LoginFailureHandler;

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
        } catch (BadCredentialsException e) {
            logger.error(ErrorCode.INVALID_CREDENTIALS.getMessage());
            throw new CustomAuthenticationException(ErrorCode.INVALID_CREDENTIALS);
        } catch (JsonProcessingException e) {
            logger.error(ErrorCode.INVALID_JSON_FORMAT.getMessage());
            throw new CustomAuthenticationException(ErrorCode.INVALID_JSON_FORMAT);
        } catch (IOException e) {
            logger.error(ErrorCode.REQUEST_BODY_READ_ERROR.getMessage());
            throw new CustomAuthenticationException(ErrorCode.REQUEST_BODY_READ_ERROR);
        } catch (Exception e) {
            logger.error(ErrorCode.UNKNOWN_ERROR.getMessage(), e);
            throw new CustomAuthenticationException(ErrorCode.UNKNOWN_ERROR, e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공
        successHandler.onAuthenticationSuccess(request, response, authentication);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 로그인 실패
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

}
