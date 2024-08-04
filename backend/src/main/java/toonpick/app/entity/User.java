package toonpick.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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

    private String username;

    private String password;

    private String role;

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
    public User(String username, String profilePicture, LocalDate accountCreationDate) {
        this.username = username;
        this.profilePicture = profilePicture;
        this.accountCreationDate = accountCreationDate;
    }

    public void update(String username, String password , String role){
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
