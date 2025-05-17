package com.toonpick.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "webtoon_statistics")
public class WebtoonStatistics {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;

    @Column(name = "episode_count", nullable = false)
    private int episodeCount = 0;

    @Column(name = "total_views", nullable = false)
    private Long totalViews = 0L;

    @Column(name = "total_likes", nullable = false)
    private Long totalLikes = 0L;

    @Column(name = "total_reviews", nullable = false)
    private Long totalReviews = 0L;

    @Column(name = "average_rating", nullable = false)
    private Float averageRating = 0.0f;

    @Column(name = "total_rating_sum", nullable = false)
    private Float totalRatingSum = 0f;

    @Column(name = "total_rating_count", nullable = false)
    private int totalRatingCount = 0;

    @Column(name = "recent_views", nullable = false)
    private Long recentViews = 0L;

    @Column(name = "recent_likes", nullable = false)
    private Long recentLikes = 0L;

    @Column(name = "recent_reviews", nullable = false)
    private Long recentReviews = 0L;

    @Column(name = "last_update_at", nullable = false)
    private LocalDateTime lastUpdateAt;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdateAt = LocalDateTime.now();
    }

    /**
     * 자동으로 One to One 연결
     */
    public WebtoonStatistics(Webtoon webtoon) {
        this.webtoon = webtoon;
        webtoon.setWebtoonStatistics(this);
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }
}
