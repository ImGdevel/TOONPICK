package com.toonpick.domain.webtoon.entity;

import com.toonpick.domain.webtoon.enums.EpisodeViewerType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "webtoon_episode_link")
public class WebtoonEpisodeLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "episode_id", nullable = false)
    private WebtoonEpisode episode;

    @ManyToOne
    @JoinColumn(name = "platform_id", nullable = false)
    private Platform platform;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "viewer_type")
    @Enumerated(EnumType.STRING)
    private EpisodeViewerType viewerType;

    @Builder
    public WebtoonEpisodeLink(WebtoonEpisode episode, Platform platform, String url, EpisodeViewerType viewerType) {
        this.episode = episode;
        this.platform = platform;
        this.url = url;
        this.viewerType = viewerType;
    }
}
