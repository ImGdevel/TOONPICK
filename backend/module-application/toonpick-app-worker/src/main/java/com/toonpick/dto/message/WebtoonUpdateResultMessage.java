package com.toonpick.dto.message;

import com.toonpick.dto.result.WebtoonEpisodeUpdateResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WebtoonUpdateResultMessage {
    private String requestId;
    private WebtoonEpisodeUpdateResult updatedWebtoon;
}
