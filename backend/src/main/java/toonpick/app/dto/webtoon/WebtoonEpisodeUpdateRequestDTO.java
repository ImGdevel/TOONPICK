package toonpick.app.dto.webtoon;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WebtoonEpisodeUpdateRequestDTO {
    private int episodeCount;
    private LocalDate lastUpdatedDate;
}
