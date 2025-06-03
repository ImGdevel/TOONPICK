package com.toonpick.entity;

import com.toonpick.enums.AuthorRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private AuthorRole role;

    @Column(name = "link")
    private String link;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AuthorSNS> authorSNSs;

    @Builder
    public Author(String uid, String name, AuthorRole role, String link) {
        this.uid = uid;
        this.name = name;
        this.role = role;
        this.link = link;
    }

    public void update(String name, AuthorRole role, String link) {
        this.name = name;
        this.role = role;
        this.link = link;
    }
}
