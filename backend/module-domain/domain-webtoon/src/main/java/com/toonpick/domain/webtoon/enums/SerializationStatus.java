package com.toonpick.domain.webtoon.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SerializationStatus {

    ONGOING("연재중"),
    HIATUS("휴재"),
    COMPLETED("완결"),
    CANCELED("연재중지"),
    ;

    private final String name;
}
