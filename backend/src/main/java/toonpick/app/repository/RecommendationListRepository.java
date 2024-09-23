package toonpick.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toonpick.app.entity.RecommendationList;

import java.util.List;

public interface RecommendationListRepository extends JpaRepository<RecommendationList, Long> {
    List<RecommendationList> findByTheme(String theme);
}
