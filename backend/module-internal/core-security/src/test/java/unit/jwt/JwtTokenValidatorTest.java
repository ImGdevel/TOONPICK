package unit.jwt;

import com.toonpick.exception.ExpiredJwtTokenException;
import com.toonpick.exception.InvalidJwtTokenException;
import com.toonpick.exception.MissingJwtTokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.toonpick.jwt.JwtTokenProvider;
import com.toonpick.jwt.JwtTokenValidator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenValidatorTest {

    @InjectMocks
    private JwtTokenValidator jwtTokenValidator;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("extractAccessToken 메서드")
    class ExtractAccessTokenTest {

        @Test
        @DisplayName("Bearer 토큰이 있으면 토큰 반환")
        void returnToken() {
            String header = "Bearer abc.def.ghi";
            String token = jwtTokenValidator.extractAccessToken(header);
            assertEquals("abc.def.ghi", token);
        }

        @Test
        @DisplayName("헤더가 null이거나 Bearer 형식이 아니면 null 반환")
        void returnNull() {
            assertNull(jwtTokenValidator.extractAccessToken(null));
            assertNull(jwtTokenValidator.extractAccessToken("Basic abc"));
        }
    }

    @Nested
    @DisplayName("validateAccessToken 메서드")
    class ValidateAccessTokenTest {

        @Test
        @DisplayName("만료된 토큰이면 예외 발생")
        void expiredAccessToken() {
            String token = "expired.token";
            when(jwtTokenProvider.isExpired(token)).thenReturn(true);

            assertThrows(ExpiredJwtTokenException.class,
                () -> jwtTokenValidator.validateAccessToken(token));
        }

        @Test
        @DisplayName("카테고리가 access가 아니면 예외 발생")
        void invalidAccessTokenCategory() {
            String token = "wrong.token";
            when(jwtTokenProvider.isExpired(token)).thenReturn(false);
            when(jwtTokenProvider.getCategory(token)).thenReturn("refresh");

            assertThrows(InvalidJwtTokenException.class,
                () -> jwtTokenValidator.validateAccessToken(token));
        }

        @Test
        @DisplayName("유효한 access 토큰은 예외 없음")
        void validAccessToken() {
            String token = "valid.token";
            when(jwtTokenProvider.isExpired(token)).thenReturn(false);
            when(jwtTokenProvider.getCategory(token)).thenReturn("access");

            assertDoesNotThrow(() -> jwtTokenValidator.validateAccessToken(token));
        }
    }

    @Nested
    @DisplayName("extractRefreshTokenFromCookies 메서드")
    class ExtractRefreshTokenFromCookiesTest {

        @Test
        @DisplayName("refresh 쿠키가 있으면 반환")
        void hasRefreshCookie() {
            Cookie[] cookies = { new Cookie("refresh", "token123") };
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getCookies()).thenReturn(cookies);

            String token = jwtTokenValidator.extractRefreshTokenFromCookies(request);
            assertEquals("token123", token);
        }

        @Test
        @DisplayName("refresh 쿠키가 없으면 null 반환")
        void noRefreshCookie() {
            Cookie[] cookies = { new Cookie("other", "value") };
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getCookies()).thenReturn(cookies);

            assertNull(jwtTokenValidator.extractRefreshTokenFromCookies(request));
        }

        @Test
        @DisplayName("쿠키가 null이면 null 반환")
        void nullCookies() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getCookies()).thenReturn(null);

            assertNull(jwtTokenValidator.extractRefreshTokenFromCookies(request));
        }
    }

    @Nested
    @DisplayName("validateRefreshToken 메서드")
    class ValidateRefreshTokenTest {

        @Test
        @DisplayName("토큰이 null 또는 빈 값이면 예외 발생")
        void nullOrEmptyToken() {
            assertThrows(MissingJwtTokenException.class,
                () -> jwtTokenValidator.validateRefreshToken(null));

            assertThrows(MissingJwtTokenException.class,
                () -> jwtTokenValidator.validateRefreshToken(""));
        }

        @Test
        @DisplayName("카테고리가 refresh가 아니면 예외 발생")
        void invalidCategory() {
            String token = "wrong.category";
            when(jwtTokenProvider.getCategory(token)).thenReturn("access");

            assertThrows(InvalidJwtTokenException.class,
                () -> jwtTokenValidator.validateRefreshToken(token));
        }

        @Test
        @DisplayName("만료된 refresh 토큰이면 예외 발생")
        void expiredRefreshToken() {
            String token = "expired.refresh";
            when(jwtTokenProvider.getCategory(token)).thenReturn("refresh");
            when(jwtTokenProvider.isExpired(token)).thenReturn(true);

            assertThrows(ExpiredJwtTokenException.class,
                () -> jwtTokenValidator.validateRefreshToken(token));
        }

        @Test
        @DisplayName("유효한 refresh 토큰이면 예외 없음")
        void validRefreshToken() {
            String token = "valid.refresh";
            when(jwtTokenProvider.getCategory(token)).thenReturn("refresh");
            when(jwtTokenProvider.isExpired(token)).thenReturn(false);

            assertDoesNotThrow(() -> jwtTokenValidator.validateRefreshToken(token));
        }
    }
}
