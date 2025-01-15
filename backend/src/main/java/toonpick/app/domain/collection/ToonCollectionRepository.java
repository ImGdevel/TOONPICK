package toonpick.app.domain.collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toonpick.app.member.entity.Member;

import java.util.List;

@Repository
public interface ToonCollectionRepository extends JpaRepository<ToonCollection, Long> {

    List<ToonCollection> findByMember(Member member);

}
