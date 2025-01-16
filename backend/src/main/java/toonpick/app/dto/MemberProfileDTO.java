package toonpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MemberProfileDTO {
    private String username;
    private String nickname;
    private String role;
    private String profilePicture;
    private String email;
    private Boolean isAdultVerified;
}
