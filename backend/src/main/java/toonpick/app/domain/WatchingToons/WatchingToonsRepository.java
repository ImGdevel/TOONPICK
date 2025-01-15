package toonpick.app.domain.WatchingToons;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import toonpick.app.domain.member.Member;
import toonpick.app.domain.webtoon.Webtoon;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchingToonsRepository extends JpaRepository<WatchingToons, Long> {

    Optional<WatchingToons> findByMemberAndWebtoon(Member member, Webtoon webtoon);

    @Query("SELECT w.webtoon FROM WatchingToons w WHERE w.member = :member")
    List<Webtoon> findWebtoonsByMember(@Param("member") Member member);

}
