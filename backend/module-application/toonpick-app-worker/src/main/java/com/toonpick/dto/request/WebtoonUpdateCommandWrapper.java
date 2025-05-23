package com.toonpick.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WebtoonUpdateCommandWrapper {
    private String requestId;
    private WebtoonUpdateCommand updatedWebtoon;
}
