package toonpick.app.auth.token;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import toonpick.app.auth.jwt.JwtTokenValidator;
import toonpick.app.auth.jwt.JwtTokenProvider;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenValidator jwtTokenValidator;

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    public String renewAccessToken(String refreshToken) {
        // Refresh 토큰 유효 검증
        refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new RuntimeException("Not found refresh token"));
        jwtTokenValidator.validateRefreshToken(refreshToken);

        // Access Token 재발급
        Long userid = jwtTokenProvider.getUserId(refreshToken);
        String username = jwtTokenProvider.getUsername(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        String newAccessToken = jwtTokenProvider.createAccessToken(userid, username, role);

        return newAccessToken;
    }

    public String renewRefreshToken(String refreshToken) {
        // Refresh 토큰 유효 검증
        refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new RuntimeException("Not found refresh token"));
        jwtTokenValidator.validateRefreshToken(refreshToken);

        // Refresh 토큰 재발급
        Long userid = jwtTokenProvider.getUserId(refreshToken);
        String username = jwtTokenProvider.getUsername(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        String newRefreshToken = jwtTokenProvider.createAccessToken(userid, username, role);

        logger.info("renew Refresh");
        // Refresh Token 저장소에 갱신
        deleteRefreshToken(refreshToken);
        saveRefreshToken(username, newRefreshToken);

        return newRefreshToken;
    }

    public void saveRefreshToken(String username, String refresh) {
        Date expirationDate = jwtTokenProvider.getExpiration(refresh);
        RefreshToken refreshToken = RefreshToken
                .builder()
                .token(refresh)
                .username(username)
                .expiration(expirationDate.toString())
                .build();

        refreshTokenRepository.save(refreshToken);

    }

    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new RuntimeException("Not found refresh token - Delete"));

        refreshTokenRepository.deleteById(refreshToken);
    }

}
