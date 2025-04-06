package unit.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.dto.LoginRequest;
import com.toonpick.exception.CustomAuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.toonpick.filter.LoginAuthenticationFilter;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginAuthenticationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LoginAuthenticationFilter loginAuthenticationFilter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("로그인 성공")
    void testSuccessfulAuthentication() throws Exception {
        // Given
        String username = "testUser";
        String password = "testPass";
        LoginRequest loginRequest = new LoginRequest(username, password);
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setContent(jsonRequest.getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication mockAuthentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(mockAuthentication);

        // When
        Authentication result = loginAuthenticationFilter.attemptAuthentication(request, response);

        // Then
        assertNotNull(result);
        verify(authenticationManager).authenticate(authToken);
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 자격 증명")
    void testUnsuccessfulAuthentication_badCredentials() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("wrongUser", "wrongPassword");
        String json = objectMapper.writeValueAsString(loginRequest);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(json.getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // then
        assertThrows(CustomAuthenticationException.class, () ->
            loginAuthenticationFilter.attemptAuthentication(request, response)
        );
    }

    @Test
    @DisplayName("로그인 실패 - JSON 파싱 실패")
    void testUnsuccessfulAuthentication_invalidJson() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("InvalidJson".getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // then
        assertThrows(CustomAuthenticationException.class, () ->
            loginAuthenticationFilter.attemptAuthentication(request, response)
        );
    }

    @Test
    @DisplayName("로그인 실패 - 알 수 없는 예외")
    void testUnsuccessfulAuthentication_unknownException() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("testUser", "password");
        String json = objectMapper.writeValueAsString(loginRequest);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(json.getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Unexpected"));

        // then
        assertThrows(CustomAuthenticationException.class, () ->
            loginAuthenticationFilter.attemptAuthentication(request, response)
        );
    }
}
