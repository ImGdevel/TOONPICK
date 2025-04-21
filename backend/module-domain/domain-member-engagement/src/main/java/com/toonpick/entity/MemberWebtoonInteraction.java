package com.toonpick.entity;

import com.toonpick.enums.WatchingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "member_webtoon_interaction", uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "webtoon_id"}))
public class MemberWebtoonInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    @Column(name = "liked", nullable = false)
    private boolean liked = false;

    @Column(name = "liked_at")
    private LocalDateTime likedAt;

    @Column(name = "bookmarked", nullable = false)
    private boolean bookmarked = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status" , nullable = false)
    private WatchingStatus status = WatchingStatus.WATCHING;

    @Column(name = "last_read_episode")
    private Integer lastReadEpisode;

    @Column(name = "last_read_at")
    private LocalDateTime lastReadAt;

    @Column(nullable = false)
    private boolean notificationEnabled;

    public void like() {
        this.liked = true;
        this.likedAt = LocalDateTime.now();
    }

    public void unlike() {
        this.liked = false;
        this.likedAt = null;
    }

    public void bookmark() {
        this.bookmarked = true;
    }

    public void unbookmark() {
        this.bookmarked = false;
    }

    public void updateStatus(WatchingStatus status) {
        this.status = status;
    }

    public void updateReading(int episode, LocalDateTime readAt) {
        this.lastReadEpisode = episode;
        this.lastReadAt = readAt;
    }

    public void setNotification(boolean enabled) {
        this.notificationEnabled = enabled;
    }
}
