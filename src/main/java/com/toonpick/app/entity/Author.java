package com.toonpick.app.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "author")
    private Set<Webtoon> webtoons;

    @Builder
    public Author(String name, Set<Webtoon> webtoons) {
        this.name = name;
        this.webtoons = webtoons;
    }

    public void update(String name, Set<Webtoon> webtoons) {
        this.name = name;
        this.webtoons = webtoons;
    }
}