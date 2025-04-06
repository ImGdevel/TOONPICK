package com.toonpick.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@RedisHash("RefreshToken")
public class RefreshToken {

    @Id
    private String token;

    private String username;

    private String expiration;

}
