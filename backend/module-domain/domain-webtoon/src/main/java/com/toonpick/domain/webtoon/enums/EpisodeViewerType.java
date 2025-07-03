package com.toonpick.domain.webtoon.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum EpisodeViewerType {
    WEB,
    MOBILE
}
