package toonpick.app.domain.collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToonCollectionRepository extends JpaRepository<ToonCollection, Long> {
}
