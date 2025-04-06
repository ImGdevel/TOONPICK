package com.toonpick.member.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberResponseDTO {
    private String username;
    private String nickname;
    private String profileImage;
    private String email;
    private Boolean isAdultVerified;
    private int level;
}
