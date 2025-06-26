package com.toonpick.worker.common.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SQSEventType {

    CRAWL_WEBTOON_ALL("crawl_webtoon_all"),
    CRAWL_WEBTOON_EPISODE("crawl_webtoon_episode"),
    CRAWL_WEBTOON_NEW("crawl_webtoon_new");

    private final String value;

    SQSEventType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static SQSEventType fromValue(String value) {
        for (SQSEventType type : SQSEventType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown SQSEventType: " + value);
    }
}
