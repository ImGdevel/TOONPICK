package com.toonpick.domain.webtoon.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum EpisodePricingType {
    FREE,
    PAID,
    WAIT_FREE
}
