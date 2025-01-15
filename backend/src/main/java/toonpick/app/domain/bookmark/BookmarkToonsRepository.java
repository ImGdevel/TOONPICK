package toonpick.app.domain.bookmark;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import toonpick.app.member.entity.Member;
import toonpick.app.domain.webtoon.Webtoon;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkToonsRepository extends JpaRepository<BookmarkToons, Long> {

    Optional<BookmarkToons> findByMemberAndWebtoon(Member member, Webtoon webtoon);

    @Query("SELECT b.webtoon FROM BookmarkToons b WHERE b.member = :member")
    List<Webtoon> findWebtoonsByMember(@Param("member") Member member);

}
