package com.toonpick.repository;


import com.toonpick.entity.FavoriteToon;
import com.toonpick.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toonpick.entity.Webtoon;

import java.util.List;
import java.util.Optional;


@Repository
public interface FavoriteToonRepository extends JpaRepository<FavoriteToon, Long> {
    Optional<FavoriteToon> findByMemberAndWebtoon(Member member, Webtoon webtoon);

    @Query("SELECT w FROM FavoriteToon m JOIN m.webtoon w WHERE m.member = :member")
    List<Webtoon> findFavoriteWebtoonsByMember(@Param("member") Member member);
}
