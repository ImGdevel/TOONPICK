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
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    private String role;

    private String name;

    private String profilePicture;

    private LocalDate accountCreationDate;

    @OneToMany(mappedBy = "user")
    private Set<InterestWebtoon> interestWebtoons;

    @OneToMany(mappedBy = "user")
    private Set<InterestAuthor> interestAuthors;

    @OneToMany(mappedBy = "user")
    private Set<InterestGenre> interestGenres;

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews;



    @Builder
    public User(String name, String profilePicture, LocalDate accountCreationDate) {
        this.name = name;
        this.profilePicture = profilePicture;
        this.accountCreationDate = accountCreationDate;
    }
}
