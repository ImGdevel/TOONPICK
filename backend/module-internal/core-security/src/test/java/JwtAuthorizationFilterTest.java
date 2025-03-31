import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.toonpick.jwt.JwtTokenProvider;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtAuthorizationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void shouldAllowRequestWithoutToken() throws Exception {
        // 테스트: 토큰 없이 요청 시 필터가 요청을 차단하지 않음
        mockMvc.perform(get("/api/protected-endpoint"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldBlockRequestWithExpiredToken() throws Exception {
        // 설정: 만료된 토큰
        String expiredToken = "Bearer expiredToken";
        given(jwtTokenProvider.isExpired(anyString())).willReturn(true);

        // 테스트: 만료된 토큰으로 요청 시 401 반환
        mockMvc.perform(get("/api/protected-endpoint")
                .header("Authorization", expiredToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("access token expired"));
    }

    @Test
    void shouldBlockRequestWithInvalidCategory() throws Exception {
        // 설정: 잘못된 카테고리의 토큰
        String invalidCategoryToken = "Bearer invalidCategoryToken";
        given(jwtTokenProvider.isExpired(anyString())).willReturn(false);
        given(jwtTokenProvider.getCategory(anyString())).willReturn("invalid");

        // 테스트: 카테고리가 "access"가 아닌 경우 400 반환
        mockMvc.perform(get("/api/protected-endpoint")
                .header("Authorization", invalidCategoryToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid access token"));
    }

    @Test
    void shouldAuthenticateValidAccessToken() throws Exception {
        // 설정: 유효한 토큰
        String validToken = "Bearer validToken";
        given(jwtTokenProvider.isExpired(anyString())).willReturn(false);
        given(jwtTokenProvider.getCategory(anyString())).willReturn("access");
        given(jwtTokenProvider.getUsername(anyString())).willReturn("testUser");
        given(jwtTokenProvider.getRole(anyString())).willReturn("ROLE_USER");

        // 테스트: 유효한 토큰으로 요청 시 요청 성공
        mockMvc.perform(get("/api/protected-endpoint")
                .header("Authorization", validToken))
                .andExpect(status().isOk());
    }

    @Test
    void shouldHandleInvalidTokenGracefully() throws Exception {
        // 설정: 예외 발생 시 필터 동작
        String invalidToken = "Bearer invalidToken";
        given(jwtTokenProvider.isExpired(anyString())).willThrow(new RuntimeException("Token parsing error"));

        // 테스트: 잘못된 토큰으로 요청 시 400 반환
        mockMvc.perform(get("/api/protected-endpoint")
                .header("Authorization", invalidToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid access token"));
    }
}
