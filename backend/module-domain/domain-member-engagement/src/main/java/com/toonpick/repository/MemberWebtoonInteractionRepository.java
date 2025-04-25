package com.toonpick.repository;


import com.toonpick.entity.Member;
import com.toonpick.entity.MemberWebtoonInteraction;
import com.toonpick.entity.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberWebtoonInteractionRepository extends JpaRepository<MemberWebtoonInteraction, Long> {

    Optional<MemberWebtoonInteraction> findByMemberAndWebtoon(Member member, Webtoon webtoon);

    boolean existsByMemberAndWebtoon(Member member, Webtoon webtoon);

    List<MemberWebtoonInteraction> findAllByMember(Member member);

    List<MemberWebtoonInteraction> findAllByWebtoon(Webtoon webtoon);

    long countByWebtoonAndLikedIsTrue(Webtoon webtoon);
}
