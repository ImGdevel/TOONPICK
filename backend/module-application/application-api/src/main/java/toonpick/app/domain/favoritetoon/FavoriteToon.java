package toonpick.app.domain.favoritetoon;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toonpick.app.domain.member.Member;
import toonpick.app.domain.webtoon.Webtoon;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "favorite_toon",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "webtoon_id"})
    }
)
public class FavoriteToon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    private LocalDateTime addedDate;

    @Builder
    public FavoriteToon(Member member, Webtoon webtoon, LocalDateTime addedDate) {
        this.member = member;
        this.webtoon = webtoon;
        this.addedDate = addedDate;
    }
}

