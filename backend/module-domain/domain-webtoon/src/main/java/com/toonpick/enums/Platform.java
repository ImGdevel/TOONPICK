package com.toonpick.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Platform {
    NAVER,
    KAKAO,
    KAKAOPAGE,
    RIDI,
    LEZHIN,
    TOOMICS,
    BOMTOON,
    MRBLUE,
    BUFF,
}
