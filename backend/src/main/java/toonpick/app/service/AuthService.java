package toonpick.app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.entity.RefreshToken;
import toonpick.app.jwt.JwtUtil;
import toonpick.app.repository.RefreshTokenRepository;

import java.util.Date;


@Service
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtUtil jwtUtil;

    public AuthService(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil){
        this. refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void saveRefreshToken(String username, String refresh) {

        Date date = new Date(System.currentTimeMillis() + 86400000L);

        RefreshToken refreshToken = RefreshToken
                .builder()
                .username(username)
                .token(refresh)
                .expiration(date.toString())
                .build();

        refreshTokenRepository.save(refreshToken);
    }


    @Transactional
    public String refreshAccessToken(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (jwtUtil.isExpired(refreshToken)) {
            throw new RuntimeException("Refresh token is expired");
        }

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        return jwtUtil.createAccessToken(username, role);
    }
}