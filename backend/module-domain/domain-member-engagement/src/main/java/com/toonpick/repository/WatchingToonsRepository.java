package com.toonpick.repository;


import com.toonpick.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toonpick.entity.WatchingToons;
import com.toonpick.entity.Webtoon;

import java.util.List;
import java.util.Optional;


@Repository
public interface WatchingToonsRepository extends JpaRepository<WatchingToons, Long> {

    Optional<WatchingToons> findByMemberAndWebtoon(Member member, Webtoon webtoon);

    @Query("SELECT w.webtoon FROM WatchingToons w WHERE w.member = :member")
    List<Webtoon> findWebtoonsByMember(@Param("member") Member member);

}
