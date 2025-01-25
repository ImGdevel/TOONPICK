package toonpick.app.dto.webtoon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toonpick.app.domain.webtoon.Webtoon;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebtoonUpdateResult {
    Webtoon webtoon;
}
