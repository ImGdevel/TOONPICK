package toonpick.app.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import toonpick.app.security.jwt.JwtTokenProvider;
import toonpick.app.service.AuthService;

import java.util.Date;

@Controller
@ResponseBody
public class TokenReissueController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(TokenReissueController.class);

    public TokenReissueController(JwtTokenProvider jwtTokenProvider, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }

    @PostMapping("/api/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 쿠키에서 refresh token 가져오기
        String refreshToken = getRefreshTokenFromCookies(request);
        if (refreshToken == null) {
            return new ResponseEntity<>("Refresh token is null", HttpStatus.BAD_REQUEST);
        }

        // Refresh 토큰 만료 여부 확인
        if (jwtTokenProvider.isExpired(refreshToken)) {
            return new ResponseEntity<>("Refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // Refresh 토큰인지 확인 (카테고리 확인)
        if (!"refresh".equals(jwtTokenProvider.getCategory(refreshToken))) {
            return new ResponseEntity<>("Invalid token category", HttpStatus.BAD_REQUEST);
        }

        try {
            // 새로운 Access 토큰 발급
            String newAccessToken = authService.refreshAccessToken(refreshToken);
            response.setHeader("Authorization", "Bearer " + newAccessToken);
            logger.info("Issued new access token: {}", newAccessToken);

            // Refresh 토큰 만료 시간 확인 후 갱신 필요 시 새 토큰 발급
            if (isRefreshTokenAboutToExpire(refreshToken)) {
                String newRefreshToken = authService.refreshRefreshToken(refreshToken);
                Cookie newRefreshCookie = jwtTokenProvider.createCookie("refresh", newRefreshToken);
                response.addCookie(newRefreshCookie);
                logger.info("Issued new refresh token: {}", newRefreshToken);
            }

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 쿠키에서 refresh token 추출
    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Refresh 토큰이 만료 임박했는지 확인
    private boolean isRefreshTokenAboutToExpire(String refreshToken) {
        Date expirationDate = jwtTokenProvider.getExpiration(refreshToken);
        long remainingTime = expirationDate.getTime() - System.currentTimeMillis();
        long oneDayInMillis = 24 * 60 * 60 * 1000;

        return remainingTime < oneDayInMillis;
    }
}
