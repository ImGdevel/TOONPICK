package com.toonpick.internal.security.utils;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

@Component
public class CookieUtils {

    private static int refreshTokenExpiration;

    @Autowired
    public void setRefreshTokenExpiration(@Value("${spring.jwt.refresh-token-expiration}") int expiration) {
        CookieUtils.refreshTokenExpiration = expiration;
    }

    /**
     * 리프레시 토큰 쿠키 생성
     */
    public static Cookie createRefreshCookie(String token) {
        return createCookie("refresh", token, refreshTokenExpiration);
    }

    /**
     * 빈 쿠키 생성 (쿠키 삭제용)
     */
    public static Cookie createEmptyCookie(String key) {
        return createCookie(key, "", 0);
    }

    /**
     * 쿠키 생성 공통 메서드
     */
    public static Cookie createCookie(String key, String value, int cookieExpiration) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(cookieExpiration);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    /**
     * Header에서 Cookie 값 추출
     */
    public static String extractCookiesFromRequest(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

        // 쿠키 가져오기
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }

    // 쿠키 추가
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    // 쿠키 삭제
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        if (request.getCookies() == null) {
            return;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    // 객체 직렬화 (Base64로 인코딩)
    public static String serialize(Object object) {
        return Base64.getEncoder().encodeToString(SerializationUtils.serialize(object));
    }

    // 객체 역직렬화 (Base64 디코딩)
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(Base64.getDecoder().decode(cookie.getValue())));
    }
}
