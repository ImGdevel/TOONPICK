package com.toonpick.entity;

import com.toonpick.enums.Gender;
import com.toonpick.enums.MemberRole;
import com.toonpick.enums.MemberStatus;
import com.toonpick.type.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member",
        indexes = @Index(name = "idx_uuid", columnList = "uid")
)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uid", nullable = false, unique = true, updatable = false)
    private UUID uuid;

    // todo: 해당 필드 rename 혹은 제거
    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, length = 100, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nick_name", unique = true, length = 30, nullable = false)
    private String nickname;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender = Gender.NONE;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private MemberRole role;

    @Column(name = "profile_image_url")
    private String profileImage;

    @Column(name = "adult_verified", nullable = false)
    private Boolean adultVerified = false;

    // 계정 활성화 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MemberStatus status = MemberStatus.ACTIVE;

    // 마지막 로그인 시점
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    // 로그인 실패 횟수
    @Column(name = "login_fail_count", nullable = false)
    private int loginFailCount = 0;

    // 이메일 인증 여부
    @Column(name = "is_email_verified", nullable = false)
    private boolean emailVerified = false;

    // 정보 제공 동의
    @Column(name = "terms_agreed_at")
    private LocalDateTime termsAgreedAt;

    @Builder
    public Member(
            Long id,
            String username,
            String email,
            String password,
            String nickname,
            MemberRole role,
            LocalDate birthday,
            Gender gender,
            String profileImage
    ) {
        this.id = id;
        this.username = username;
        this.uuid = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role != null ? role : MemberRole.ROLE_USER;
        this.birthday = birthday;
        this.gender = gender;
        this.profileImage = profileImage;
        this.status = MemberStatus.ACTIVE;
        this.loginFailCount = 0;
        this.emailVerified = false;
        this.adultVerified = false;
    }

    // 로그인
    public void loginSuccess() {
        this.lastLoginAt = LocalDateTime.now();
        this.loginFailCount = 0;
    }

    // 로그인 실패
    public void loginFail() {
        this.loginFailCount++;
    }

    // 로그인 실패 초기화
    public void resetLoginFailCount() {
        this.loginFailCount = 0;
    }

    // 개인 정보 동의
    public void agreeTerms() {
        this.termsAgreedAt = LocalDateTime.now();
    }

    // 프로필 업데이트
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 프로필 이미지 업데이트
    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    // 패스워드 변경
    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException(ErrorCode.PASSWORD_TOO_SHORT.getMessage());
        }
        this.password = newPassword;
    }

    // 성인 인증
    public void verifyAdult() {
        this.adultVerified = true;
    }

    // 이메일 인증
    public void verifyEmail() {
        this.emailVerified = true;
    }

    // 계정 활성화
    public void activateAccount() {
        this.status = MemberStatus.ACTIVE;
    }

    // 휴면 계정 처리
    public void suspendAccount() {
        this.status = MemberStatus.SUSPENDED;
    }

    // 계정 삭제 (비활성화)
    public void deactivateAccount() {
        this.status = MemberStatus.DELETED;
    }
}
