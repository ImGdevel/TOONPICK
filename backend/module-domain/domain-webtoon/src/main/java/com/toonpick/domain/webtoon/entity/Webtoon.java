package com.toonpick.domain.webtoon.entity;

import com.toonpick.domain.webtoon.enums.AgeRating;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "webtoon",
    indexes = {
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_day_of_week", columnList = "day_of_week")
    }
)
public class Webtoon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "normalized_title", nullable = false)
    private String normalizedTitle;

    @Column(name = "external_id", nullable = true)
    private String externalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SerializationStatus serializationStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "age_rating", nullable = false)
    private AgeRating ageRating;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "publish_start_date")
    private LocalDate publishStartDate;

    @Column(name = "last_update_date")
    private LocalDate lastUpdatedDate;

    @OneToMany(mappedBy = "webtoon", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("rank ASC")
    private List<WebtoonPlatform> platforms = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "webtoon_author",
            joinColumns = @JoinColumn(name = "webtoon_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "webtoon_genre",
            joinColumns = @JoinColumn(name = "webtoon_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @OneToOne(
            mappedBy = "webtoon",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private WebtoonStatistics statistics;

    @Builder
    public Webtoon(Long id,
                   String title,
                   String externalId,
                   DayOfWeek dayOfWeek,
                   String thumbnailUrl,
                   AgeRating ageRating,
                   String summary,
                   SerializationStatus serializationStatus,
                   LocalDate publishStartDate,
                   LocalDate lastUpdatedDate,
                   List<WebtoonPlatform> platforms,
                   Set<Author> authors,
                   Set<Genre> genres
    ) {
        this.id = id;
        this.title = title;
        this.externalId = externalId;
        this.dayOfWeek = dayOfWeek;
        this.thumbnailUrl = thumbnailUrl;
        this.ageRating = ageRating;
        this.summary = summary;
        this.serializationStatus = serializationStatus;
        this.publishStartDate = publishStartDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.platforms = platforms != null ? platforms : new ArrayList<>();
        this.authors = authors != null ? authors : new HashSet<>();
        this.genres = genres != null ? genres : new HashSet<>();
    }

    public void updateWebtoonDetails(String title,
                                     String description,
                                     SerializationStatus serializationStatus,
                                     DayOfWeek dayOfWeek,
                                     String thumbnailUrl,
                                     AgeRating ageRating,
                                     Set<Author> authors,
                                     Set<Genre> genres) {
        this.title = title;
        this.summary = description;
        this.serializationStatus = serializationStatus;
        this.dayOfWeek = dayOfWeek;
        this.thumbnailUrl = thumbnailUrl;
        this.ageRating = ageRating;
        this.platforms = platforms != null ? platforms : new ArrayList<>();
        this.authors = authors != null ? authors : this.authors;
        this.genres = genres != null ? genres : this.genres;
    }

    @PrePersist
    @PreUpdate
    private void prePersistAndUpdate() {
        this.normalizedTitle = this.title != null ? this.title.replaceAll(" ", "") : null;
    }

    public void setWebtoonStatistics(WebtoonStatistics statistics){
        this.statistics = statistics;
    }
}
