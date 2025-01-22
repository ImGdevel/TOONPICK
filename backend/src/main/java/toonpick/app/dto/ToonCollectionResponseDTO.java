package toonpick.app.dto;

import lombok.Builder;
import lombok.Data;
import toonpick.app.dto.member.MemberProfileResponseDTO;
import toonpick.app.dto.webtoon.WebtoonResponseDTO;

import java.util.List;

@Data
@Builder
public class ToonCollectionResponseDTO {
    private String title;
    private MemberProfileResponseDTO member;
    private List<WebtoonResponseDTO> webtoons;
}
