package toonpick.app.unit.security.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import toonpick.app.exception.exception.RefreshTokenNotFoundException;
import toonpick.app.security.jwt.JwtTokenProvider;
import toonpick.app.security.jwt.JwtTokenValidator;
import toonpick.app.security.token.RefreshToken;
import toonpick.app.security.token.RefreshTokenRepository;
import toonpick.app.security.token.TokenService;

import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private JwtTokenValidator jwtTokenValidator;

    @InjectMocks
    private TokenService tokenService;

    @Test
    @DisplayName("Access 토큰 발급")
    public void testIssueAccessToken() {
        when(jwtTokenProvider.createAccessToken(any(), any())).thenReturn("new.access.token");

        String result = tokenService.issueAccessToken("user", "ROLE_USER");

        assertEquals("new.access.token", result);
        verify(jwtTokenProvider, times(1)).createAccessToken("user", "ROLE_USER");
    }


    @Test
    @DisplayName("Refresh 토큰 발급")
    public void testIssueRefreshToken() {
        when(jwtTokenProvider.createRefreshToken(any(), any())).thenReturn("new.refresh.token");
        when(jwtTokenProvider.getExpiration(any())).thenReturn(new Date()); // Mock Expiration Date 설정

        String result = tokenService.issueRefreshToken("user", "ROLE_USER");

        assertEquals("new.refresh.token", result);
        verify(jwtTokenProvider, times(1)).createRefreshToken("user", "ROLE_USER");
    }


    @Test
    @DisplayName("Access 토큰 재발급")
    public void testReissueAccessToken() {
        when(refreshTokenRepository.findById(any())).thenReturn(Optional.of(new RefreshToken()));
        when(jwtTokenProvider.getUsername(any())).thenReturn("user");
        when(jwtTokenProvider.getRole(any())).thenReturn("ROLE_USER");
        when(jwtTokenProvider.createAccessToken(any(), any())).thenReturn("new.access.token");

        String result = tokenService.reissueAccessToken("valid.refresh.token");

        assertEquals("new.access.token", result);
    }

    @Test
    @DisplayName("만료된 Refresh 토큰 예외 처리")
    public void testReissueAccessToken_ExpiredToken() {
        when(refreshTokenRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class, () -> {
            tokenService.reissueAccessToken("expired.refresh.token");
        });
    }
}
