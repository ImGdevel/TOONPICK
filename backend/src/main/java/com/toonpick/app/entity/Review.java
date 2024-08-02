package com.toonpick.app.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "webtoon_id")
    private Webtoon webtoon;

    private LocalDate reviewDate;

    private float rating;

    @Column(length = 1000)
    private String comment;

    private int likes;

    @Builder
    public Review(User user, Webtoon webtoon, LocalDate reviewDate, float rating, String comment, int likes) {
        this.user = user;
        this.webtoon = webtoon;
        this.reviewDate = reviewDate;
        this.rating = rating;
        this.comment = comment;
        this.likes = likes;
    }

    public void update(LocalDate reviewDate, float rating, String comment, int likes) {
        this.reviewDate = reviewDate;
        this.rating = rating;
        this.comment = comment;
        this.likes = likes;
    }
}