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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "webtoon_similar_webtoon")
public class SimilarWebtoon extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_webtoon_id", nullable = false)
    private Webtoon sourceWebtoon;

    @ManyToOne
    @JoinColumn(name = "similar_webtoon_id", nullable = false)
    private Webtoon similarWebtoon;

    @Column(name = "score", nullable = false)
    private float score = 0f;

    @Column(name = "rank", nullable = false)
    private int rank;
}
