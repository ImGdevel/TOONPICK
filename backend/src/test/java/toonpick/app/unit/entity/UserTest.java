package toonpick.app.unit.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toonpick.app.entity.User;

import static org.assertj.core.api.Assertions.assertThat;


class UserTest {

    @Test
    @DisplayName("User 엔티티 생성 및 초기 값 확인")
    void testUserCreation() {
        User user = User.builder()
                .username("testUser")
                .email("test@example.com")
                .password("password")
                .nickname("testNickname")
                .role("ROLE_USER")
                .isAdultVerified(false)
                .profilePicture("default_profile_img.png")
                .build();

        assertThat(user.getUsername()).isEqualTo("testUser");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getNickname()).isEqualTo("testNickname");
    }

    @Test
    @DisplayName("프로필 업데이트 테스트")
    void testUpdateProfile() {
        User user = User.builder()
                .nickname("testNickname")
                .profilePicture("default_profile_img.png")
                .build();

        user.updateProfile("newNickname", "newProfilePicture.png");

        assertThat(user.getNickname()).isEqualTo("newNickname");
        assertThat(user.getProfilePicture()).isEqualTo("newProfilePicture.png");
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void testChangePassword() {
        User user = User.builder()
                .password("oldPassword")
                .build();

        user.changePassword("newPassword");

        assertThat(user.getPassword()).isEqualTo("newPassword");
    }

    @Test
    @DisplayName("성인 인증 상태 변경 테스트")
    void testVerifyAdult() {
        User user = User.builder()
                .isAdultVerified(false)
                .build();

        user.verifyAdult();

        assertThat(user.getIsAdultVerified()).isTrue();
    }
}

