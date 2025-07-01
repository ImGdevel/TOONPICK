package com.toonpick.domain.member.entity;

import com.toonpick.domain.member.entity.Member;
import com.toonpick.domain.webtoon.entity.Webtoon;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "toon_collection")
public class ToonCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "toon_collection_webtoon",
        joinColumns = @JoinColumn(name = "toon_collection_id"),
        inverseJoinColumns = @JoinColumn(name = "webtoon_id")
    )
    private List<Webtoon> webtoons = new ArrayList<>();

    @Builder
    public ToonCollection(Member member, String title) {
        this.member = member;
        this.title = title;
    }

    public void addWebtoon(Webtoon webtoon) {
        this.webtoons.add(webtoon);
    }

    public void removeWebtoon(Webtoon webtoon) {
        this.webtoons.remove(webtoon);
    }

    public void clearWebtoons() {
        this.webtoons.clear();
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
