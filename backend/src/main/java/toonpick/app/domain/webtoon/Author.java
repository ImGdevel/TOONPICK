package toonpick.app.domain.webtoon;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "uid", nullable = false, unique = true)
    private String uid;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    private String role;

    private String link;

    @Builder
    public Author(String uid, String name, String role, String link) {
        this.uid = uid;
        this.name = name;
        this.role = role;
        this.link = link;
    }

    public void update(String name, String role, String link) {
        this.name = name;
        this.role = role;
        this.link = link;
    }
}
