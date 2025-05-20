package com.toonpick.enums;

public enum Provider {
    GOOGLE,
    NAVER,
    KAKAO;

    public static Provider fromString(String provider) {
        return Provider.valueOf(provider.toUpperCase());
    }
}
