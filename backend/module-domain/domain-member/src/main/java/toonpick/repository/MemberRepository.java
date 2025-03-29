package toonpick.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toonpick.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    Optional<Member> findByUsername(String username);
}
