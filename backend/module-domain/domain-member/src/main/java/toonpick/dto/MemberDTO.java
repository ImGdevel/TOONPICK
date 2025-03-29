package toonpick.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDTO {
    private String username;
    private String role;
    private String email;
}
