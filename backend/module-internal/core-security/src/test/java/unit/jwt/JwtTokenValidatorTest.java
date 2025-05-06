package unit.jwt;

import com.toonpick.exception.ExpiredJwtTokenException;
import com.toonpick.exception.InvalidJwtTokenException;
import com.toonpick.exception.MissingJwtTokenException;
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
