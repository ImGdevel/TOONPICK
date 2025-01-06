package toonpick.app.auth.filter;

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
import toonpick.app.auth.exception.ExpiredJwtTokenException;
import toonpick.app.auth.exception.MissingJwtTokenException;
import toonpick.app.auth.jwt.JwtTokenValidator;
import toonpick.app.auth.exception.InvalidJwtTokenException;
import toonpick.app.common.utils.ErrorResponseSender;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenValidator jwtTokenValidator;
    private final ErrorResponseSender errorResponseSender;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    // todo : 문제 상황) 인증 헤더 검증은 언제 이루어져야하는가?
    // todo : 방법1과 아래 방법2를 비교하여 어떤 방법이 가장 효율적인지 체크할 것
    // todo : 추후 다른 대안을 모색하거나 선택된 방법 이외의 코드는 제거할 것

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // todo : 방법 1. 인증이 필요 없는 경로 건너뛰기 (임시)
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/api/public/") || requestUri.equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

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
            // todo : 방법 2. 잘못된 JWT의 경우 세션 정보를 지우고 다음 필터로 전송
            logger.warn("Invalid JWT Token {}", e.getMessage());
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            logger.error("Authorization failed: {}", e.getMessage());
            errorResponseSender.sendErrorResponse(response, "Authentication error", HttpServletResponse.SC_FORBIDDEN);
        }

        filterChain.doFilter(request, response);
    }

}
