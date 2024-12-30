package toonpick.app.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toonpick.app.entity.BaseTimeEntity;
import toonpick.app.member.entity.Member;
import toonpick.app.webtoon.entity.Webtoon;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "webtoon_review")
public class WebtoonReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;

    @Column(nullable = false)
    private float rating;

    @Column(length = 1000)
    private String comment;

    private int likes;

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
