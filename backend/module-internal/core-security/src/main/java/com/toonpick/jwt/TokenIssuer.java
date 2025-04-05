package com.toonpick.jwt;

public interface TokenIssuer {
    String issueAccessToken(String username, String role);
    String issueRefreshToken(String username, String role);
    String reissueAccessToken(String username);
    String reissueRefreshToken(String username);
    void deleteRefreshToken(String username);
}
