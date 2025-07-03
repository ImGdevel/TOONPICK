package com.toonpick.domain.member.enums;

public enum Provider {
    GOOGLE,
    NAVER,
    KAKAO;

    public static Provider fromString(String provider) {
        return Provider.valueOf(provider.toUpperCase());
    }
}
