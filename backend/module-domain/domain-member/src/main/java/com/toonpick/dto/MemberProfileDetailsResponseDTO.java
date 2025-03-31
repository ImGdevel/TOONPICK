package com.toonpick.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberProfileDetailsResponseDTO {
    private String username;
    private String nickname;
    private String profilePicture;
    private String email;
    private Boolean isAdultVerified;
    private int level;

    // todo : 프로필 상세 페이지에 필요한 데이터
}
