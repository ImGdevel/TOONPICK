package toonpick.app.dto.webtoon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toonpick.app.domain.webtoon.enums.Platform;
import toonpick.app.domain.webtoon.enums.SerializationStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebtoonUpdateRequest {
    private Long id;
    private String title;
    private String link;
    private Platform platform;
    private SerializationStatus serializationStatus;
}
