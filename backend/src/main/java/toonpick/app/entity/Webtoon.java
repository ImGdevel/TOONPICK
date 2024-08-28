package toonpick.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String title;

    private String thumbnailUrl;

    private String url;


    @Column(length = 3000)
    private String description;

    private int episodeCount;

    private LocalDate serializationStartDate;

    @Enumerated(EnumType.STRING)
    private DayOfWeek week;

    private float averageRating;

    private float platformRating;

    private String ageRating;

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

    @OneToMany(mappedBy = "webtoon")
    private Set<Review> reviews = new HashSet<>();

    @Builder

    public Webtoon(String title, float averageRating, float platformRating, String description, int episodeCount, LocalDate serializationStartDate, DayOfWeek week, String thumbnailUrl, String url, String ageRating, Set<Author> authors, Set<Genre> genres) {
        this.title = title;
        this.averageRating = averageRating;
        this.platformRating = platformRating;
        this.description = description;
        this.episodeCount = episodeCount;
        this.serializationStartDate = serializationStartDate;
        this.week = week;
        this.thumbnailUrl = thumbnailUrl;
        this.url = url;
        this.ageRating = ageRating;
        this.authors = authors != null ? authors : new HashSet<>();
        this.genres = genres != null ? genres : new HashSet<>();
    }

    public void update(String title, float averageRating, float platformRating, String description, int episodeCount, LocalDate serializationStartDate, DayOfWeek serializationDay, String thumbnailUrl, String url, String ageRating, Set<Author> authors, Set<Genre> genres) {
        this.title = title;
        this.averageRating = averageRating;
        this.platformRating = platformRating;
        this.description = description;
        this.episodeCount = episodeCount;
        this.serializationStartDate = serializationStartDate;
        this.week = serializationDay;
        this.thumbnailUrl = thumbnailUrl;
        this.url = url;
        this.ageRating = ageRating;
        this.authors = authors != null ? authors : new HashSet<>();
        this.genres = genres != null ? genres : new HashSet<>();
    }
}
