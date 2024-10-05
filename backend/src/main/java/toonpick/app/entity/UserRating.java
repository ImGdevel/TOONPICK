package toonpick.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_rating")
public class UserRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;

    @Column(nullable = false)
    private float rating;

    @Column(length = 1000)
    private String comment;

    private int likes;

    private LocalDate modifyDate;

    @Builder
    public UserRating(User user, Webtoon webtoon, float rating, String comment, int likes, LocalDate modifyDate) {
        this.user = user;
        this.webtoon = webtoon;
        this.rating = rating;
        this.comment = comment;
        this.likes = likes;
        this.modifyDate = modifyDate;
    }

    public void update(float rating, String comment, LocalDate modifyDate) {
        this.rating = rating;
        this.comment = comment;
        this.modifyDate = modifyDate;
    }

    // 좋아요 수 업데이트 메서드
    public void updateLikes(int likes) {
        this.likes = likes;
    }
}
