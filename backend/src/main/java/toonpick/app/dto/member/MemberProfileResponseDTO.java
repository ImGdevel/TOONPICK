package toonpick.app.dto.member;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberProfileResponseDTO {
    private String username;
    private String nickname;
    private String profilePicture;
    private int level;
}
