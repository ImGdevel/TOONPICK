package toonpick.app.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import toonpick.app.controller.TokenReissueController;
import toonpick.app.dto.CustomUserDetails;
import toonpick.app.entity.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(TokenReissueController.class);

    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Access 토큰 인증/인가
        String accessToken = request.getHeader("Authorization");

        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7).trim();
        }
        // Access 토큰 존재 여부 체크, 없다면 다음 필터체인으로
        if (accessToken == null || accessToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 토큰 만료 확인
            if (jwtTokenProvider.isExpired(accessToken)) {
                logger.warn("Access token expired");
                sendErrorResponse(response, "access token expired", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            // 토큰의 카테고리 확인
            String category = jwtTokenProvider.getCategory(accessToken);
            if (category == null || !category.equals("access")) {
                logger.warn("Invalid access token category");
                sendErrorResponse(response, "invalid access token", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // 토큰에서 사용자 정보 추출 및 인증
            String username = jwtTokenProvider.getUsername(accessToken);
            String role = jwtTokenProvider.getRole(accessToken);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = createUserDetails(username, role);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("Authentication successful for user: " + username);
            }
        } catch (Exception e) {
            logger.error("Exception occurred while parsing JWT token", e);
            sendErrorResponse(response, "invalid access token", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        filterChain.doFilter(request, response);
    }

    // 에러 응답을 JSON 형식으로 보내는 메서드
    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", message);

        PrintWriter writer = response.getWriter();
        writer.write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(responseBody));
        writer.flush();
    }

    // CustomUserDetails 생성
    private UserDetails createUserDetails(String username, String role) {
        User user = new User();
        user.update(username, "dummyPassword", role);
        return new CustomUserDetails(user);
    }
}
