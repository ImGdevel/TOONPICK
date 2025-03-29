package toonpick.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import toonpick.entity.Member;
import toonpick.entity.ToonCollection;

import java.util.List;


@Repository
public interface ToonCollectionRepository extends JpaRepository<ToonCollection, Long> {

    List<ToonCollection> findByMember(Member member);

}
