package com.toonpick.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "webtoon_season")
public class WebtoonSeason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    @Column(name = "season_no", nullable = false)
    private int seasonNumber = 1;

    @Column(name = "title")
    private String title;

    @Column(name = "started_at")
    private LocalDate startDate;

    @Column(name = "ended_at")
    private LocalDate endDate;

    @Builder
    public WebtoonSeason(Webtoon webtoon, int seasonNumber, String title, LocalDate startDate, LocalDate endDate) {
        this.webtoon = webtoon;
        this.seasonNumber = seasonNumber;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
