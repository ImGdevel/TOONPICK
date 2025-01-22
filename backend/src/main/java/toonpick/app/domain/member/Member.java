package toonpick.app.domain.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toonpick.app.domain.BaseTimeEntity;

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

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY ,cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberStatistics statistics;

    @Builder
    public Member(String username, String email, String password, String nickname, MemberRole role,
                  Boolean isAdultVerified, String profileImage) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.isAdultVerified = isAdultVerified;
        this.profileImage = profileImage;
    }

    public void updateProfile(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profilePicture){
        this.profileImage = profilePicture;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void verifyAdult() {
        this.isAdultVerified = true;
    }

    public void updateLevel(int newLevel) {
        this.level = newLevel;
    }
}
