package com.toonpick.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SerializationStatus {

    ONGOING("연재중"),
    HIATUS("휴재"),
    COMPLETED("완결"),
    CANCELLED("연재중지"),
    ;

    private final String name;
}
