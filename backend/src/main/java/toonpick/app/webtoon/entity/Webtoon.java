package toonpick.app.webtoon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toonpick.app.entity.WebtoonReview;
import toonpick.app.entity.enums.AgeRating;
import toonpick.app.entity.enums.Platform;
import toonpick.app.entity.enums.SerializationStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "webtoon")
public class Webtoon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Column(unique = true)
    private String platformId;

    @NotNull
    private String title;

    private String titleWithoutSpaces;

    private String thumbnailUrl;

    @NotNull
    private String url;

    @Enumerated(EnumType.STRING)
    private SerializationStatus serializationStatus;

    @Enumerated(EnumType.STRING)
    private AgeRating ageRating;

    @Column(length = 3000)
    private String description;

    private int episodeCount;

    private LocalDate serializationStartDate;

    private LocalDate lastUpdatedDate;

    @Enumerated(EnumType.STRING)
    private DayOfWeek week;

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

    @OneToMany(mappedBy = "webtoon", fetch = FetchType.LAZY)
    private Set<WebtoonReview> userRatings = new HashSet<>();

    @Version
    private int version;

    private float platformRating = 0;

    private float averageRating = 0;

    private float ratingSum = 0;

    private int ratingCount = 0;

    @Builder
    public Webtoon(Long id, String title, Platform platform, String platformId, float ratingSum, int ratingCount, float averageRating, String description, SerializationStatus serializationStatus, int episodeCount, LocalDate serializationStartDate, LocalDate lastUpdatedDate, DayOfWeek week, String thumbnailUrl, String url, AgeRating ageRating, Set<Author> authors, Set<Genre> genres) {
        this.id = id;
        this.title = title;
        this.titleWithoutSpaces = title != null ? title.replaceAll(" ", "") : null;
        this.platform = platform;
        this.platformId = platformId;
        this.ratingSum = ratingSum;
        this.ratingCount = ratingCount;
        this.averageRating = averageRating;
        this.description = description;
        this.serializationStatus = serializationStatus;
        this.episodeCount = episodeCount;
        this.serializationStartDate = serializationStartDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.week = week;
        this.thumbnailUrl = thumbnailUrl;
        this.url = url;
        this.ageRating = ageRating;
        this.authors = authors != null ? authors : new HashSet<>();
        this.genres = genres != null ? genres : new HashSet<>();
    }

    public void addRating(float newRating) {
        this.ratingSum += newRating;
        this.ratingCount += 1;
        this.averageRating = this.ratingSum / this.ratingCount;
    }

    public void update(String title, Platform platform, float averageRating, String description, int episodeCount, LocalDate serializationStartDate, LocalDate lastUpdatedDate, SerializationStatus serializationStatus, DayOfWeek week, String thumbnailUrl, String url, AgeRating ageRating, Set<Author> authors, Set<Genre> genres) {
        this.title = title;
        this.titleWithoutSpaces = title != null ? title.replaceAll(" ", "") : this.titleWithoutSpaces;
        this.platform = platform;
        this.averageRating = averageRating;
        this.description = description;
        this.serializationStatus = serializationStatus;
        this.episodeCount = episodeCount;
        this.serializationStartDate = serializationStartDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.week = week;
        this.thumbnailUrl = thumbnailUrl;
        this.url = url;
        this.ageRating = ageRating;
        this.authors = authors != null ? authors : this.authors;
        this.genres = genres != null ? genres : this.genres;
    }
}
