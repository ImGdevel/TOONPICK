package toonpick.app.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
/* 이전 코드
        String authorization = request.getHeader("Authorization");

        // 헤더 검증
        if(authorization == null || !authorization.startsWith("Bearer")){
            System.out.println("token null");
            filterChain.doFilter(request, response); // 다음 필터로 전달
            return;
        }

        String token = authorization.split(" ")[1];
*/
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            System.out.println(cookie.getName());
            if (cookie.getName().equals("Authorization")) {

                authorization = cookie.getValue();
            }
        }

        if(authorization == null){
            System.out.println("token null");
            filterChain.doFilter(request, response); // 다음 필터로 전달
            return;
        }

        String token = authorization;
        if(jwtUtils.isExpired(token)){
            System.out.println("token expired");
            filterChain.doFilter(request, response); // 다음 필터로 전달
            return;
        }

        String username = jwtUtils.getUsername(token);
        String role = jwtUtils.getRole(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = createUserDetails(username, role);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        }
        
        filterChain.doFilter(request, response);
    }

    private UserDetails createUserDetails(String username, String role) {
        User user = new User();
        user.update(username, "dummyPassword", role); // 패스워드는 더미 값으로 설정
        return new CustomUserDetails(user);
    }
}
