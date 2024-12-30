package toonpick.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toonpick.app.webtoon.entity.Webtoon;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recommendation_list")
public class RecommendationList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String theme;

    private String description;

    @ManyToMany
    @JoinTable(
            name = "recommendation_webtoons",
            joinColumns = @JoinColumn(name = "recommendation_list_id"),
            inverseJoinColumns = @JoinColumn(name = "webtoon_id")
    )
    private Set<Webtoon> webtoons = new HashSet<>();

    @Builder
    public RecommendationList(Long id, String theme, String description, Set<Webtoon> webtoons) {
        this.id = id;
        this.theme = theme;
        this.description = description;
        this.webtoons = webtoons != null ? webtoons : new HashSet<>();
    }

    public void update(String theme, String description, Set<Webtoon> webtoons) {
        this.theme = theme;
        this.description = description;
        this.webtoons = webtoons != null ? webtoons : new HashSet<>();
    }
}
