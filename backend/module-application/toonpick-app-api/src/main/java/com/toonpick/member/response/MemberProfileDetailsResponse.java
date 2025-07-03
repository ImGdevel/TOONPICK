package com.toonpick.member.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberProfileDetailsResponse {
    private String username;
    private String nickname;
    private String profileImage;
    private String email;
    private int level;
    private Boolean isAdultVerified;

    // todo : 프로필 상세 페이지에 필요한 데이터
}
