package toonpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toonpick.app.domain.webtoon.enums.Platform;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebtoonUpdateRequest {
    private String title;
    private String link;
    private Platform platform;
}
