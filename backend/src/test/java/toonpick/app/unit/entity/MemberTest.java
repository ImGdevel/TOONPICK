package toonpick.app.unit.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toonpick.app.domain.member.Member;
import toonpick.app.domain.member.MemberRole;

import static org.assertj.core.api.Assertions.assertThat;


class MemberTest {

    @Test
    @DisplayName("Member 엔티티 생성 및 초기 값 확인")
    void testUserCreation() {
        Member member = Member.builder()
                .username("testUser")
                .email("test@example.com")
                .password("password")
                .nickname("testNickname")
                .role(MemberRole.ROLE_USER)
                .isAdultVerified(false)
                .profilePicture("default_profile_img.png")
                .build();

        assertThat(member.getUsername()).isEqualTo("testUser");
        assertThat(member.getEmail()).isEqualTo("test@example.com");
        assertThat(member.getNickname()).isEqualTo("testNickname");
    }

    @Test
    @DisplayName("프로필 업데이트 테스트")
    void testUpdateProfile() {
        Member member = Member.builder()
                .nickname("testNickname")
                .profilePicture("default_profile_img.png")
                .build();

        member.updateProfile("newNickname", "newProfilePicture.png");

        assertThat(member.getNickname()).isEqualTo("newNickname");
        assertThat(member.getProfilePicture()).isEqualTo("newProfilePicture.png");
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void testChangePassword() {
        Member member = Member.builder()
                .password("oldPassword")
                .build();

        member.changePassword("newPassword");

        assertThat(member.getPassword()).isEqualTo("newPassword");
    }

    @Test
    @DisplayName("성인 인증 상태 변경 테스트")
    void testVerifyAdult() {
        Member member = Member.builder()
                .isAdultVerified(false)
                .build();

        member.verifyAdult();

        assertThat(member.getIsAdultVerified()).isTrue();
    }
}

