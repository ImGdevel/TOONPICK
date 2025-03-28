package toonpick.app.dto.webtoon;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WebtoonUpdateBatchRequestDTO {
    private String requestId;
    private int totalCount;
    private List<WebtoonUpdateRequestDTO> webtoons;
}
