package com.toonpick.domain.webtoon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "webtoon_platform")
public class WebtoonPlatform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "platform_id", nullable = false)
    private Platform platform;

    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "rank")
    private Integer rank; // 플랫폼 정렬 순위

    @Builder
    public WebtoonPlatform(Webtoon webtoon, Platform platform, String link, Integer rank) {
        this.webtoon = webtoon;
        this.platform = platform;
        this.link = link;
        this.rank = rank;
    }
}
