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

    private float averageRating;

    private float platformRating;

    @Column(length = 5000)
    private String description;

    private int episodeCount;

    private LocalDate serializationStartDate;

    @Enumerated(EnumType.STRING)
    private DayOfWeek serializationDay;

    private String thumbnailUrl; // 추가된 필드
    private String url;          // 추가된 필드
    private String ageRating;    // 추가된 필드

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
    public Webtoon(String title, float averageRating, float platformRating, String description, int episodeCount, LocalDate serializationStartDate, DayOfWeek serializationDay, String thumbnailUrl, String url, String ageRating, Set<Author> authors, Set<Genre> genres) {
        this.title = title;
        this.averageRating = averageRating;
        this.platformRating = platformRating;
        this.description = description;
        this.episodeCount = episodeCount;
        this.serializationStartDate = serializationStartDate;
        this.serializationDay = serializationDay;
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
        this.serializationDay = serializationDay;
        this.thumbnailUrl = thumbnailUrl;
        this.url = url;
        this.ageRating = ageRating;
        this.authors = authors != null ? authors : new HashSet<>();
        this.genres = genres != null ? genres : new HashSet<>();
    }
}
