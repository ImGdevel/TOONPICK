package com.toonpick.dto;

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
