package com.toonpick.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "webtoon_review",
    uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "webtoon_id"}))
public class WebtoonReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    @Column(name = "rating", nullable = false)
    private float rating;

    @Size(max = 1000)
    @Column(length = 1000)
    private String comment;

    @Column(name = "like_count", nullable = false)
    private int likesCount;

    @Column(name = "spoilable", nullable = false)
    private boolean spoiler;

    @Column(name = "rating_count", nullable = false)
    private int reportCount;

    @Column(name = "reported", nullable = false)
    private boolean reported;

    // === 도메인 메서드 ===

    public void updateRating(float newRating) {
        this.rating = newRating;
    }

    public void updateComment(String newComment) {
        this.comment = newComment;
    }

    public void addLike() {
        this.likesCount++;
    }

    public void report() {
        this.reported = true;
        this.reportCount++;
    }

    public void unreport() {
        this.reported = false;
    }
}
