package com.toonpick.toon_collection.response;

import com.toonpick.member.response.MemberProfileResponse;
import com.toonpick.webtoon.response.WebtoonSummaryResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ToonCollectionResponseDTO {
    private String title;
    private MemberProfileResponse member;
    private List<WebtoonSummaryResponse> webtoons;
}
