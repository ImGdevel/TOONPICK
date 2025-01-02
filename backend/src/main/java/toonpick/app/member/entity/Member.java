package toonpick.app.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toonpick.app.common.utils.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member", indexes = @Index(name = "idx_username", columnList = "username"))
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String nickname;

    private String role;

    private Boolean isAdultVerified;

    private String profilePicture;

    @Builder
    public Member(String username, String email, String password, String nickname, String role,
                  Boolean isAdultVerified, String profilePicture) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.isAdultVerified = isAdultVerified;
        this.profilePicture = profilePicture;
    }

    public void updateProfile(String nickname, String profilePicture) {
        this.nickname = nickname;
        this.profilePicture = profilePicture;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void verifyAdult() {
        this.isAdultVerified = true;
    }
}
