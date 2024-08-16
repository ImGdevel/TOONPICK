package toonpick.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinRequestDTO {
    private String username;
    private String password;
}
