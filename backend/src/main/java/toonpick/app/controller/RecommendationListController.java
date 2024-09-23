package toonpick.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.dto.RecommendationListDTO;
import toonpick.app.service.RecommendationListService;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationListController {

    private final RecommendationListService recommendationListService;

    public RecommendationListController(RecommendationListService recommendationListService) {
        this.recommendationListService = recommendationListService;
    }

    @PostMapping
    public ResponseEntity<RecommendationListDTO> createRecommendationList(@RequestBody RecommendationListDTO recommendationListDTO) {
        return ResponseEntity.ok(recommendationListService.createRecommendationList(recommendationListDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecommendationListDTO> updateRecommendationList(@PathVariable Long id, @RequestBody RecommendationListDTO recommendationListDTO) {
        return ResponseEntity.ok(recommendationListService.updateRecommendationList(id, recommendationListDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecommendationList(@PathVariable Long id) {
        recommendationListService.deleteRecommendationList(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RecommendationListDTO>> getAllRecommendationLists() {
        return ResponseEntity.ok(recommendationListService.getAllRecommendationLists());
    }
}
