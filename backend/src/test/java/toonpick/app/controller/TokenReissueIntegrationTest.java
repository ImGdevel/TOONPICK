package toonpick.app.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import toonpick.app.security.jwt.JwtTokenProvider;
import toonpick.app.security.jwt.JwtTokenValidator;
import toonpick.app.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenReissueIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenValidator jwtTokenValidator;

    @Test
    @DisplayName("유효한 Refresh Token으로 Access Token 갱신 요청 성공")
    void testReissue_Success() throws Exception {
        String refreshToken = "valid-refresh-token";
        String newAccessToken = "new-access-token";

        when(jwtTokenValidator.extractRefreshTokenFromCookies(any())).thenReturn(refreshToken);
        doNothing().when(jwtTokenValidator).validateRefreshToken(refreshToken);
        when(authService.refreshAccessToken(refreshToken)).thenReturn(newAccessToken);

        mockMvc.perform(post("/api/reissue")
                .cookie(new Cookie("refresh", refreshToken)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bearer " + newAccessToken));
    }

    @Test
    @DisplayName("유효하지 않은 Refresh Token으로 요청 시 BAD_REQUEST 반환")
    void testReissue_InvalidToken() throws Exception {
        String invalidRefreshToken = "invalid-refresh-token";

        when(jwtTokenValidator.extractRefreshTokenFromCookies(any())).thenReturn(invalidRefreshToken);
        doThrow(new IllegalArgumentException("Invalid token")).when(jwtTokenValidator).validateRefreshToken(invalidRefreshToken);

        mockMvc.perform(post("/api/reissue")
                .cookie(new Cookie("refresh", invalidRefreshToken)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid token"));
    }
}
