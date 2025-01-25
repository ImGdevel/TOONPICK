package toonpick.app.domain.webtoon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toonpick.app.domain.review.WebtoonReview;
import toonpick.app.domain.webtoon.enums.AgeRating;
import toonpick.app.domain.webtoon.enums.Platform;
import toonpick.app.domain.webtoon.enums.SerializationStatus;

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
    private String titleWithoutSpaces;

    @NotNull
    @Column(name = "platform", nullable = false, length = 8)
    private Platform platform;

    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @NotNull
    @Column(name = "status", nullable = false)
    private SerializationStatus serializationStatus;

    @NotNull
    @Column(name = "age_rating", nullable = false)
    private AgeRating ageRating;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @NotNull
    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "episode_count")
    private int episodeCount;

    @Column(name = "publish_start_date")
    private LocalDate publishStartDate;

    @Column(name = "publish_last_date")
    private LocalDate publishLastDate;

    @Column(name = "last_update_date")
    private LocalDate lastUpdatedDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "webtoon_author",
            joinColumns = @JoinColumn(name = "webtoon_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "webtoon_genre",
            joinColumns = @JoinColumn(name = "webtoon_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "webtoon", fetch = FetchType.LAZY)
    private Set<WebtoonReview> userRatings = new HashSet<>();

    private float platformRating = 0;

    private float averageRating = 0;

    private float ratingSum = 0;

    private int ratingCount = 0;

    @Version
    private int version;

    @Builder
    public Webtoon(Long id,
                   String externalId,
                   String title,
                   Platform platform,
                   DayOfWeek dayOfWeek,
                   String thumbnailUrl,
                   String link,
                   AgeRating ageRating,
                   String description,
                   SerializationStatus serializationStatus,
                   int episodeCount,
                   float platformRating,
                   LocalDate publishStartDate,
                   LocalDate publishLastDate,
                   LocalDate lastUpdatedDate,
                   Set<Author> authors,
                   Set<Genre> genres) {
        this.id = id;
        this.externalId = externalId;
        this.title = title;
        this.platform = platform;
        this.dayOfWeek = dayOfWeek;
        this.thumbnailUrl = thumbnailUrl;
        this.link = link;
        this.ageRating = ageRating;
        this.description = description;
        this.serializationStatus = serializationStatus;
        this.platformRating = platformRating;
        this.episodeCount = episodeCount;
        this.publishStartDate = publishStartDate;
        this.publishLastDate = publishLastDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.authors = authors != null ? authors : new HashSet<>();
        this.genres = genres != null ? genres : new HashSet<>();
    }

    public void updateEpisodeCountAndDate(int episodeCount,
                                          LocalDate publishLastDate,
                                          LocalDate lastUpdatedDate) {
        this.episodeCount = episodeCount;
        this.publishLastDate = publishLastDate;
        this.lastUpdatedDate = lastUpdatedDate;
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
        this.description = description;
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
        this.titleWithoutSpaces = this.title != null ? this.title.replaceAll(" ", "") : null;
    }
}
