package toonpick.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toonpick.app.entity.Member;
import toonpick.app.entity.MemberFavoriteWebtoon;
import toonpick.app.entity.Webtoon;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberFavoriteWebtoonRepository extends JpaRepository<MemberFavoriteWebtoon, Long> {
    Optional<MemberFavoriteWebtoon> findByMemberIdAndWebtoonId(Long memberId, Long webtoonId);

    List<Webtoon> findByMember(Member member);
}
