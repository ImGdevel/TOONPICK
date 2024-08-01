package com.toonpick.app.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "webtoon")
public class Webtoon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private float averageRating;

    private float platformRating;

    private String description;

    private int episodeCount;

    private LocalDate serializationStartDate;

    private String serializationDay;

    @ManyToMany
    @JoinTable(
            name = "webtoon_author",
            joinColumns = @JoinColumn(name = "webtoon_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors;

    @ManyToMany
    @JoinTable(
            name = "webtoon_genre",
            joinColumns = @JoinColumn(name = "webtoon_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    @OneToMany(mappedBy = "webtoon")
    private Set<Review> reviews;

    @Builder
    public Webtoon(String title, float averageRating, float platformRating, String description, int episodeCount, LocalDate serializationStartDate, String serializationDay) {
        this.title = title;
        this.averageRating = averageRating;
        this.platformRating = platformRating;
        this.description = description;
        this.episodeCount = episodeCount;
        this.serializationStartDate = serializationStartDate;
        this.serializationDay = serializationDay;
    }

    public void update(String title, float averageRating, float platformRating, String description, int episodeCount, LocalDate serializationStartDate, String serializationDay, Set<Author> authors, Set<Genre> genres) {
        this.title = title;
        this.averageRating = averageRating;
        this.platformRating = platformRating;
        this.description = description;
        this.episodeCount = episodeCount;
        this.serializationStartDate = serializationStartDate;
        this.serializationDay = serializationDay;
        this.authors = authors;
        this.genres = genres;
    }
}
