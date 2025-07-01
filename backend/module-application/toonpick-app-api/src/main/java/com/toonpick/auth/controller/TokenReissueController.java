package com.toonpick.auth.controller;

import com.toonpick.internal.security.jwt.JwtTokenProvider;
import com.toonpick.internal.security.jwt.JwtTokenValidator;
import com.toonpick.internal.security.jwt.TokenIssuer;
import com.toonpick.internal.security.utils.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Tag(name = "Security - Token", description = "보안 토큰 재발급 (접근 권한=public)")
@Controller
@ResponseBody
@RequiredArgsConstructor
public class TokenReissueController {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenIssuer tokenIssuer;
    private final JwtTokenValidator jwtTokenValidator;

    private static final Logger logger = LoggerFactory.getLogger(TokenReissueController.class);

    @Operation(summary = "access 토큰 재발급", description = "refresh 토큰을 통해 access 토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 refresh token 추출 및 검증
        String refreshToken = CookieUtils.extractCookiesFromRequest(request, "refresh");
        jwtTokenValidator.validateRefreshToken(refreshToken);

        // 새로운 Access 토큰 발급
        String newAccessToken = tokenIssuer.reissueAccessToken(refreshToken);
        response.setHeader("Authorization", "Bearer " + newAccessToken);

        // Refresh 토큰 갱신 필요 여부 확인 및 갱신
        if (jwtTokenProvider.isRefreshTokenAboutToExpire(refreshToken)) {
            String newRefreshToken = tokenIssuer.reissueRefreshToken(refreshToken);
            response.addCookie(CookieUtils.createRefreshCookie(newRefreshToken));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
