package com.toonpick.entity;

import com.toonpick.enums.Gender;
import com.toonpick.enums.MemberRole;
import com.toonpick.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member", indexes = @Index(name = "idx_username", columnList = "user_name"))
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nick_name", unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private MemberRole role;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "is_adult_verified")
    private Boolean isAdultVerified = false;

    @Column(name = "level")
    private int level = 0;

    // 출성년도 (나이)
    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    // 계정 활성화 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MemberStatus status = MemberStatus.ACTIVE;

    // 마지막 로그인 시점
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    // 로그인 실패 횟수
    @Column(name = "login_fail_count")
    private int loginFailCount = 0;

    // 이메일 인증 여부
    @Column(name = "is_email_verified")
    private boolean isEmailVerified = false;

    // 정보 제공 동의
    @Column(name = "agreed_terms_at")
    private LocalDateTime agreedTermsAt;

    // 맴버 사용 통계
    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY ,cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberStatistics statistics;

    @Builder
    public Member(String username,
                  String email,
                  String password,
                  String nickname,
                  MemberRole role,
                  Boolean isAdultVerified,
                  String profileImage,
                  LocalDate birthday,
                  String phoneNumber,
                  Gender gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.isAdultVerified = isAdultVerified != null ? isAdultVerified : false;
        this.profileImage = profileImage;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.status = MemberStatus.ACTIVE;
        this.loginFailCount = 0;
        this.isEmailVerified = false;
    }

    // 로그인
    public void login() {
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
        this.agreedTermsAt = LocalDateTime.now();
    }

    // 프로필 업데이트
    public void updateProfile(String nickname) {
        this.nickname = nickname;
    }

    // 프로필 이미지 업데이트
    public void updateProfileImage(String profilePicture){
        this.profileImage = profilePicture;
    }

    // 패스워드 변경
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // 성인 인증
    public void verifyAdult() {
        this.isAdultVerified = true;
    }

    // 이메일 인증
    public void verifyEmail() {
        this.isEmailVerified = true;
    }

    // 레벨 업데이트
    public void updateLevel(int newLevel) {
        this.level = newLevel;
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
