package com.toonpick.internal.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JoinRequest {
    private String username;
    private String email;
    private String password;
}
