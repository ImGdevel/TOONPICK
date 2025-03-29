package toonpick.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberRole {
    ROLE_USER("USER"),
    ROLE_AUTHOR("AUTHOR"),
    ROLE_ADMIN("ADMIN");

    private final String role;

    @Override
    public String toString() {
        return role;
    }
}
