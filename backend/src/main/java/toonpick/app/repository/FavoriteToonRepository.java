package toonpick.app.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import toonpick.app.domain.favoritetoon.FavoriteToon;
import toonpick.app.domain.member.Member;
import toonpick.app.domain.webtoon.Webtoon;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteToonRepository extends JpaRepository<FavoriteToon, Long> {
    Optional<FavoriteToon> findByMemberAndWebtoon(Member member, Webtoon webtoon);

    @Query("SELECT w FROM FavoriteToon m JOIN m.webtoon w WHERE m.member = :member")
    List<Webtoon> findFavoriteWebtoonsByMember(@Param("member") Member member);
}
