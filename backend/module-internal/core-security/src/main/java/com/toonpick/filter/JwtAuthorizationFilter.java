package com.toonpick.filter;

import com.toonpick.exception.ExpiredJwtTokenException;
import com.toonpick.exception.InvalidJwtTokenException;
import com.toonpick.exception.MissingJwtTokenException;
import com.toonpick.utils.ErrorResponseSender;
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
    private final ErrorResponseSender errorResponseSender;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        logger.info("url: {}", requestUri);

        try {
            // 요청 헤더에서 Access Token 확인
            String accessToken = jwtTokenValidator.extractAccessToken(request.getHeader("Authorization"));

            if (accessToken != null) {
                // Access Token 검증 및 유저 인증 정보 추출
                jwtTokenValidator.validateAccessToken(accessToken);
                UserDetails userDetails = jwtTokenValidator.getUserDetails(accessToken);

                // SecurityContext 인증 정보가 없으면 인증 정보 설정
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                logger.info("Authorization successful for user: {}", userDetails.getUsername());
            }

        } catch (ExpiredJwtTokenException | InvalidJwtTokenException | MissingJwtTokenException e){
            logger.warn("Invalid JWT Token {}", e.getMessage());
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            logger.error("Authorization failed: {}", e.getMessage());
            errorResponseSender.sendErrorResponse(response, "Authentication error", HttpServletResponse.SC_FORBIDDEN);
        }

        filterChain.doFilter(request, response);
    }

}
