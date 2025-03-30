package com.toonpick.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberProfileRequestDTO {
    private String nickname;
}
