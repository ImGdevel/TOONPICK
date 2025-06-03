package com.toonpick.entity;

import com.toonpick.enums.EpisodePricingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "webtoon_episode")
public class WebtoonEpisode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private WebtoonSeason webtoonSeason;

    @Column(name = "episode_no", nullable = false)
    private int episodeNumber;

    @Column(name = "title")
    private String title;

    @Column(name = "pricing_type")
    private EpisodePricingType pricingType;

    @OneToMany(mappedBy = "episode", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<WebtoonEpisodeLink> episodeUrls = new ArrayList<>();
}