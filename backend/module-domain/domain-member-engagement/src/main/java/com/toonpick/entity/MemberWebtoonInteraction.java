package com.toonpick.entity;

import com.toonpick.enums.WatchingStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Webtoon webtoon;

    private Boolean liked;
    private Boolean bookmarked;

    @Enumerated(EnumType.STRING)
    private WatchingStatus status;

    private Integer lastReadEpisode;
    private LocalDateTime lastReadAt;

    private LocalDateTime likedAt;
    private Integer scrollPosition;

    private String tags;
    private Boolean notificationEnabled;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_review")
    private WebtoonReview webtoonReview;
}
