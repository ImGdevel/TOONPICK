package com.toonpick.app.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    private float rating;

    @Column(length = 1000)
    private String description;

    private String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "webtoon_genre",
            joinColumns = @JoinColumn(name = "webtoon_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;



    @Builder
    public Webtoon(String title, Author author, float rating, String description, String imageUrl ,Set<Genre> genres) {
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.description = description;
        this.imageUrl = imageUrl;
        this.genres = genres;
    }

    public void update(String title, Author author, float rating, String description, String imageUrl, Set<Genre> genres) {
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.description = description;
        this.imageUrl = imageUrl;
        this.genres = genres;
    }
}
