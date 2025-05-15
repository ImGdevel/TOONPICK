package com.toonpick.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.toonpick.enums.AgeRating;
import com.toonpick.enums.Platform;
import com.toonpick.enums.SerializationStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "webtoon",
    indexes = {
        @Index(name = "idx_platform", columnList = "platform"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_day_of_week", columnList = "day_of_week")
    }
)
public class Webtoon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true)
    private String externalId;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "normalized_title", nullable = false)
    private String normalizedTitle;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false)
    private Platform platform;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

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

    @NotNull
    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "episode_count")
    private int episodeCount;

    @Column(name = "publish_start_date")
    private LocalDate publishStartDate;

    @Column(name = "last_update_date")
    private LocalDate lastUpdatedDate;

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

    @Column(name = "platform_rating")
    private Float platformRating = 0f;

    @Column(name = "average_rating")
    private Float averageRating = 0f;

    @Column(name = "rating_sum")
    private Float ratingSum = 0f;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    @OneToOne(
            mappedBy = "webtoon",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private WebtoonStatistics webtoonStatistics;

    @Builder
    public Webtoon(Long id,
                   String externalId,
                   String title,
                   Platform platform,
                   DayOfWeek dayOfWeek,
                   String thumbnailUrl,
                   String link,
                   AgeRating ageRating,
                   String summary,
                   SerializationStatus serializationStatus,
                   int episodeCount,
                   float platformRating,
                   LocalDate publishStartDate,
                   LocalDate lastUpdatedDate,
                   Set<Author> authors,
                   Set<Genre> genres) {
        this.id = id;
        this.externalId = platform + externalId;
        this.title = title;
        this.platform = platform;
        this.dayOfWeek = dayOfWeek;
        this.thumbnailUrl = thumbnailUrl;
        this.link = link;
        this.ageRating = ageRating;
        this.summary = summary;
        this.serializationStatus = serializationStatus;
        this.platformRating = platformRating;
        this.episodeCount = episodeCount;
        this.publishStartDate = publishStartDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.authors = authors != null ? authors : new HashSet<>();
        this.genres = genres != null ? genres : new HashSet<>();
    }

    public void updateWebtoonDetails(String title,
                                     Platform platform,
                                     String description,
                                     SerializationStatus serializationStatus,
                                     DayOfWeek dayOfWeek,
                                     String thumbnailUrl,
                                     String link,
                                     AgeRating ageRating,
                                     Set<Author> authors,
                                     Set<Genre> genres) {
        this.title = title;
        this.platform = platform;
        this.summary = description;
        this.serializationStatus = serializationStatus;
        this.dayOfWeek = dayOfWeek;
        this.thumbnailUrl = thumbnailUrl;
        this.link = link;
        this.ageRating = ageRating;
        this.authors = authors != null ? authors : this.authors;
        this.genres = genres != null ? genres : this.genres;
    }

    @PrePersist
    @PreUpdate
    private void prePersistAndUpdate() {
        this.normalizedTitle = this.title != null ? this.title.replaceAll(" ", "") : null;
    }
}
