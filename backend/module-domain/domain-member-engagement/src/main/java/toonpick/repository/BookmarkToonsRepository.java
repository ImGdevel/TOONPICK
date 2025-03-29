package toonpick.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import toonpick.entity.BookmarkToons;
import toonpick.entity.Member;
import toonpick.entity.Webtoon;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkToonsRepository extends JpaRepository<BookmarkToons, Long> {

    Optional<BookmarkToons> findByMemberAndWebtoon(Member member, Webtoon webtoon);

    @Query("SELECT b.webtoon FROM BookmarkToons b WHERE b.member = :member")
    List<Webtoon> findWebtoonsByMember(@Param("member") Member member);

}
