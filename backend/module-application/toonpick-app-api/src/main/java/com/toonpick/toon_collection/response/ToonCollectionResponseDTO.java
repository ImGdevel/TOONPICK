package com.toonpick.toon_collection.response;

import com.toonpick.member.response.MemberProfileResponseDTO;
import com.toonpick.webtoon.response.WebtoonResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ToonCollectionResponseDTO {
    private String title;
    private MemberProfileResponseDTO member;
    private List<WebtoonResponseDTO> webtoons;
}
