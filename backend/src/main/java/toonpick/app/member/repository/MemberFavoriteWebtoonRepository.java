package toonpick.app.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toonpick.app.member.entity.Member;
import toonpick.app.member.entity.MemberFavoriteWebtoon;
import toonpick.app.webtoon.entity.Webtoon;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberFavoriteWebtoonRepository extends JpaRepository<MemberFavoriteWebtoon, Long> {
    Optional<MemberFavoriteWebtoon> findByMemberAndWebtoon(Member member, Webtoon webtoon);

    List<Webtoon> findByMember(Member member);
}
