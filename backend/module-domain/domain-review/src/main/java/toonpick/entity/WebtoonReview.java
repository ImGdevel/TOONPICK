package toonpick.entity;

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

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    @Column(name = "rating", nullable = false)
    private float rating = 0;

    @Column(name = "comment", length = 1000)
    private String comment;

    @Column(name = "likes")
    private int likes = 0;

    @Builder
    public WebtoonReview(Member member, Webtoon webtoon, float rating, String comment, int likes) {
        this.member = member;
        this.webtoon = webtoon;
        this.rating = rating;
        this.comment = comment;
        this.likes = likes;
    }

    public void update(float rating, String comment){
        this.rating = rating;
        this.comment = comment;
    }

}
