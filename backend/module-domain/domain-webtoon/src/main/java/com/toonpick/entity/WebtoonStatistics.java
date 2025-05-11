package com.toonpick.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "webtoon_statistics")
public class WebtoonStatistics {

    @Id
    @Column(name = "webtoon_id")
    private Long webtoonId;

    @Column(name = "total_views", nullable = false)
    private Long totalViews = 0L;

    @Column(name = "total_likes", nullable = false)
    private Long totalLikes = 0L;

    @Column(name = "total_reviews", nullable = false)
    private Long totalReviews = 0L;

    @Column(name = "average_rating", nullable = false)
    private Float averageRating = 0.0f;

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
}
