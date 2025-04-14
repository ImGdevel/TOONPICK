package com.toonpick.member.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberProfileResponseDTO {
    private String username;
    private String nickname;
    private String profileImage;
    private int level;
}
