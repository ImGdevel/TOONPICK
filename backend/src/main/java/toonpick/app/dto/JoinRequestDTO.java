package toonpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JoinRequestDTO {
    private String username;
    private String email;
    private String password;
}
