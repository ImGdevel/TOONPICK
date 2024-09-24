package toonpick.app.entity;

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

    private String username;

    private String password;

    private String email;

    private String role;

    private String profilePicture;

    private LocalDate accountCreationDate;


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
