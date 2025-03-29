package toonpick.app.domain.webtoon.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Platform {
    NAVER,
    KAKAO,
    RIDI,
    LEZHIN,
    TOOMICS,
    MRBLUE,
    BUFF
}
