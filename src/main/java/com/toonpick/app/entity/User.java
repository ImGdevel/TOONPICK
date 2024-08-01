package com.toonpick.app.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    private String role;

    private String name;

    private String profilePicture;

    private LocalDate accountCreationDate;

    @OneToMany(mappedBy = "user")
    private Set<InterestWebtoon> interestWebtoons = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<InterestAuthor> interestAuthors = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<InterestGenre> interestGenres = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews = new HashSet<>();



    @Builder
    public User(String name, String profilePicture, LocalDate accountCreationDate) {
        this.name = name;
        this.profilePicture = profilePicture;
        this.accountCreationDate = accountCreationDate;
    }
}
