package toonpick.app.security.token;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("RefreshToken")
public class RefreshToken {

    @Id
    private String token;

    private String username;

    private String expiration;

}
