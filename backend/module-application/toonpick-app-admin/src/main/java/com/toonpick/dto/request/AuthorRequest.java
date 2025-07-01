package com.toonpick.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.toonpick.domain.webtoon.enums.AuthorRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthorRequest {
    @JsonProperty("uid")
    private String uid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("role")
    private AuthorRole role;
}
