package toonpick.app.domain.bookmark;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toonpick.app.common.utils.BaseTimeEntity;
import toonpick.app.domain.member.Member;
import toonpick.app.domain.webtoon.Webtoon;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bookmark_toon",
    indexes = @Index(name = "idx_member", columnList = "member_id"),
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "webtoon_id"})
    })
public class BookmarkToons extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    @Builder
    public BookmarkToons(Member member, Webtoon webtoon){
        this.member = member;
        this.webtoon = webtoon;
    }

}
