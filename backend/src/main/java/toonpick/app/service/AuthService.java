package toonpick.app.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.entity.RefreshToken;
import toonpick.app.entity.User;
import toonpick.app.security.jwt.JwtTokenProvider;
import toonpick.app.repository.RefreshTokenRepository;
import toonpick.app.repository.UserRepository;

import java.util.Date;


@Service
public class AuthService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,RefreshTokenRepository refreshTokenRepository, JwtTokenProvider jwtTokenProvider){
        this.userRepository = userRepository;
        this. refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
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

        if (jwtTokenProvider.isExpired(refreshToken)) {
            throw new RuntimeException("Refresh token is expired");
        }

        Long userid = jwtTokenProvider.getUserId(refreshToken);
        String username = jwtTokenProvider.getUsername(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        return jwtTokenProvider.createAccessToken(userid, username, role);
    }

    @Transactional
    public String refreshRefreshToken(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (jwtTokenProvider.isExpired(refreshToken)) {
            throw new RuntimeException("Refresh token is expired");
        }

        Long userid = jwtTokenProvider.getUserId(refreshToken);
        String username = jwtTokenProvider.getUsername(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        String newRefreshToken = jwtTokenProvider.createAccessToken(userid, username, role);

        deleteRefreshToken(refreshToken);
        saveRefreshToken(username,newRefreshToken);

        return newRefreshToken;
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenRepository.deleteByToken(refreshToken);
    }

    @Transactional
    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return user.getId();
    }
}
