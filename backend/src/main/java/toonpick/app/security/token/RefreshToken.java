package toonpick.app.security.token;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@RedisHash("RefreshToken")
public class RefreshToken {

    @Id
    private String token;

    private String username;

    private String expiration;

}
