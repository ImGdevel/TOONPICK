package com.toonpick.test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.toonpick.filter.LoginAuthenticationFilter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class LoginAuthenticationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void testSuccessfulAuthentication() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("""
                {
                    "username": "testUser",
                    "password": "testPassword"
                }
                """.getBytes());

        MockHttpServletResponse response = new MockHttpServletResponse();

        LoginAuthenticationFilter filter = new LoginAuthenticationFilter(authenticationManager, null, null);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("testUser", "testPassword");

        when(authenticationManager.authenticate(authenticationToken)).thenReturn(mock(Authentication.class));

        filter.attemptAuthentication(request, response);

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    @DisplayName("로그인 실패 - 유효하지 않은 데이터")
    void testUnsuccessfulAuthentication() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("InvalidContent".getBytes());

        MockHttpServletResponse response = new MockHttpServletResponse();

        LoginAuthenticationFilter filter = new LoginAuthenticationFilter(authenticationManager, null, null);

        assertThatThrownBy(() -> filter.attemptAuthentication(request, response))
                .isInstanceOf(Exception.class);
    }
}
