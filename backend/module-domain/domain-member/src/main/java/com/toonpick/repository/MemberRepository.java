package com.toonpick.repository;

import com.toonpick.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    Optional<Member> findByUsername(String username);
}
