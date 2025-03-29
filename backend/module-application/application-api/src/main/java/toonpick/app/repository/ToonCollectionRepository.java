package toonpick.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toonpick.app.domain.toon_collection.ToonCollection;
import toonpick.app.domain.member.Member;

import java.util.List;

@Repository
public interface ToonCollectionRepository extends JpaRepository<ToonCollection, Long> {

    List<ToonCollection> findByMember(Member member);

}
