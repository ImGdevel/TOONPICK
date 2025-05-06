package com.toonpick.filter;

import com.toonpick.dto.CustomUserDetails;
import com.toonpick.exception.CustomAuthenticationException;
import com.toonpick.exception.ExpiredJwtTokenException;
import com.toonpick.exception.InvalidJwtTokenException;
import com.toonpick.jwt.JwtTokenProvider;
import com.toonpick.type.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import com.toonpick.jwt.JwtTokenValidator;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenValidator jwtTokenValidator;
    private final JwtTokenProvider jwtTokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 헤더에서 Access Token 확인
        String accessToken = jwtTokenValidator.extractAccessToken(request.getHeader("Authorization"));

        if (accessToken != null) {
            try {
                jwtTokenValidator.validateAccessToken(accessToken);
                authenticateUser(accessToken);
            } catch (ExpiredJwtTokenException | InvalidJwtTokenException e) {
                logger.warn("Invalid JWT Token: {}", e.getMessage());
                throw new CustomAuthenticationException(ErrorCode.INVALID_AUTH_TOKEN);
            }
        }

        filterChain.doFilter(request, response);
    }

    // SecurityContext에 인증 정보 설정
    private void authenticateUser(String accessToken) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = getUserDetails(accessToken);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    // 토큰에서 유저 정보 추출
    private UserDetails getUserDetails(String token) {
        String username = jwtTokenProvider.getUsername(token);
        String role = jwtTokenProvider.getRole(token);
        return new CustomUserDetails(username, "", role);
    }
}
