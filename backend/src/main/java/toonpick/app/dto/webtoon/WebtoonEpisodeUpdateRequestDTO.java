package toonpick.app.dto.webtoon;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class WebtoonEpisodeUpdateRequestDTO {
    private int episodeCount;
    private LocalDate lastUpdatedDate;
}
