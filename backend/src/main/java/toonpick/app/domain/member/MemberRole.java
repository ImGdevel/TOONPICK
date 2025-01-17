package toonpick.app.domain.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberRole {
    ROLE_USER,
    ROLE_AUTHOR,
    ROLE_ADMIN
}
