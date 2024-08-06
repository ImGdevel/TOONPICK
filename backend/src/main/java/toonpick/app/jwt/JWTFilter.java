package toonpick.app.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import toonpick.app.dto.CustomUserDetails;
import toonpick.app.entity.User;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;

    public JWTFilter (JWTUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        // 헤더 검증
        if(authorization == null || !authorization.startsWith("Bearer")){
            System.out.println("token null");
            filterChain.doFilter(request, response); // 다음 필터로 전달
            return;
        }

        String token = authorization.split(" ")[1];
        if(jwtUtils.isExpired(token)){
            System.out.println("token null");
            filterChain.doFilter(request, response); // 다음 필터로 전달
            return;
        }

        String username = jwtUtils.getUsername(token);
        String role = jwtUtils.getRole(token);

        User user = new User();
        user.update(username, "temp", role);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        // todo : 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        // todo : 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        filterChain.doFilter(request, response);
    }
}
