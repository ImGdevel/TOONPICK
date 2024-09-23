package toonpick.app.jwt;

import io.jsonwebtoken.ExpiredJwtException;
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
import toonpick.app.controller.ReissueController;
import toonpick.app.dto.CustomUserDetails;
import toonpick.app.entity.User;

import java.io.IOException;
import java.io.PrintWriter;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(ReissueController.class);

    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("Access valid");
        String accessToken = request.getHeader("Authorization");

        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7).trim();
            logger.info("Extracted Access Token: " + accessToken);
        } else {
            logger.warn("Bearer token not found or malformed");
        }

        if (accessToken == null || accessToken.isEmpty()) {
            logger.warn("No Access Token found");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtTokenProvider.isExpired(accessToken)) {
                logger.warn("Access token expired");
                sendErrorResponse(response, "access token expired");
                return;
            }
            String category = jwtTokenProvider.getCategory(accessToken);
            if (category == null || !category.equals("access")) {
                logger.warn("Invalid access token category");
                sendErrorResponse(response, "invalid access token");
                return;
            }
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
            sendErrorResponse(response, "invalid access token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.print(message);
    }


    private UserDetails createUserDetails(String username, String role) {
        User user = new User();
        user.update(username, "dummyPassword", role); // 패스워드는 더미 값으로 설정
        return new CustomUserDetails(user);
    }
}
