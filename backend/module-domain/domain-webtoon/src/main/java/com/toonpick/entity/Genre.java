package com.toonpick.entity;

import com.toonpick.enums.GenreType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "type", nullable = false)
    private GenreType type = GenreType.SUB;

    @NotNull
    @Column(name = "display", nullable = false)
    private boolean display = false;

    @Builder
    public Genre(String name) {
        this.name = name;
    }

    public void promoteToMain() {
        this.type = GenreType.MAIN;
        this.display = true;
    }

    public void demoteToSub() {
        this.type = GenreType.SUB;
    }

    public void enableDisplay() {
        this.display = true;
    }

    public void disableDisplay() {
        this.display = false;
    }
}
