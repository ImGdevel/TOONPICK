package toonpick.app.security.filter;

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
import toonpick.app.security.jwt.JwtTokenValidator;
import toonpick.app.util.ErrorResponseSender;

import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenValidator jwtTokenValidator;
    private final ErrorResponseSender errorResponseSender;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    public JwtAuthorizationFilter(JwtTokenValidator jwtTokenValidator, ErrorResponseSender errorResponseSender) {
        this.jwtTokenValidator = jwtTokenValidator;
        this.errorResponseSender = errorResponseSender;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = jwtTokenValidator.extractAccessToken(request.getHeader("Authorization"));

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwtTokenValidator.validateAccessToken(accessToken);

            UserDetails userDetails = jwtTokenValidator.getUserDetails(accessToken);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("Authorization successful for user: {}", userDetails.getUsername());
            }
        } catch (Exception e) {
            logger.warn("Authorization failed: {}", e.getMessage());
            errorResponseSender.sendErrorResponse(response, e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
