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
import toonpick.app.security.jwt.JwtTokenValidator;
import toonpick.app.service.AuthService;

import java.util.Date;

@Controller
@ResponseBody
public class TokenReissueController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    private final JwtTokenValidator jwtTokenValidator;
    private static final Logger logger = LoggerFactory.getLogger(TokenReissueController.class);

    public TokenReissueController(JwtTokenProvider jwtTokenProvider, AuthService authService, JwtTokenValidator jwtTokenValidator) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
        this.jwtTokenValidator = jwtTokenValidator;
    }

    @PostMapping("/api/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 쿠키에서 refresh token 추출 및 검증
            String refreshToken = jwtTokenValidator.extractRefreshTokenFromCookies(request);
            jwtTokenValidator.validateRefreshToken(refreshToken);

            // 새로운 Access 토큰 발급
            String newAccessToken = authService.refreshAccessToken(refreshToken);
            response.setHeader("Authorization", "Bearer " + newAccessToken);
            logger.info("Issued new access token");

            // Refresh 토큰 갱신 필요 여부 확인 및 갱신
            if (jwtTokenProvider.isRefreshTokenAboutToExpire(refreshToken)) {
                String newRefreshToken = authService.refreshRefreshToken(refreshToken);
                Cookie newRefreshCookie = jwtTokenProvider.createCookie("refresh", newRefreshToken);
                response.addCookie(newRefreshCookie);
                logger.info("Issued new refresh token");
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.error("Token validation error: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
