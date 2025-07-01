package com.toonpick.domain.webtoon.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum AuthorRole {

    WRITER("글"),
    ARTIST("그림"),
    BOTH("글/그림"),
    ORIGINAL("원작"),
    ;

    private final String name;

}
