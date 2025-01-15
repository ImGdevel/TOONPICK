package toonpick.app.domain.webtoon;

import jakarta.persistence.*;
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

    @Column(unique = true)
    private String name;

    private String role;

    private String link;

    @Builder
    public Author(String name, String role, String link) {
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
