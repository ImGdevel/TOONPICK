package toonpick.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "webtoon_review")
public class WebtoonReview extends BaseTimeEntity {

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

    @Version
    private int version; // 버전 필드 추가

    @Builder
    public WebtoonReview(User user, Webtoon webtoon, float rating, String comment, int likes) {
        this.user = user;
        this.webtoon = webtoon;
        this.rating = rating;
        this.comment = comment;
        this.likes = likes;
    }


    public void update(float rating, String comment){
        this.rating = rating;
        this.comment = comment;
    }

    public void increaseLikes() {
        this.likes++;
    }

    public void decreaseLikes() {
        if (this.likes > 0) {
            this.likes--;
        }
    }
}
