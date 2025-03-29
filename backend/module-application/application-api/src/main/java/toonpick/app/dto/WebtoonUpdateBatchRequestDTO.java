package toonpick.app.dto;

import lombok.Builder;
import lombok.Data;
import toonpick.dto.WebtoonUpdateRequestDTO;

import java.util.List;

@Data
@Builder
public class WebtoonUpdateBatchRequestDTO {
    private String requestId;
    private int totalCount;
    private List<WebtoonUpdateRequestDTO> webtoons;
}
