package com.toonpick.domain.member.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "member_statistics")
public class MemberStatistics {

    @Id
    private Long memberId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "level", nullable = false)
    private int level = 0;

    @Column(name = "activity_score", nullable = false)
    private long activityScore = 0;

    @Column(name = "total_viewed_webtoon_count", nullable = false)
    private int totalViewedWebtoonCount = 0;

    @Column(name = "total_liked_webtoon_count", nullable = false)
    private int totalLikedWebtoonCount = 0;

    @Column(name = "total_login_day_count", nullable = false)
    private int totalLoginDayCount = 0;

    @Column(name = "total_webtoon_review_count", nullable = false)
    private int totalWebtoonReviewCount = 0;

    @Column(name = "average_rating", nullable = false)
    private int averageRating = 0;

    @Column(name = "total_like_received_count", nullable = false)
    private int totalLikeReceivedCount = 0;

    @Column(name = "total_reposted_count", nullable = false)
    private int totalRepostedCount = 0;

    @Column(name = "total_reported_review_count", nullable = false)
    private int totalReportedReviewCount = 0;

}
