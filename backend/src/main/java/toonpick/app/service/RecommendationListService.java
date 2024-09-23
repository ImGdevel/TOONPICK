package toonpick.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.dto.RecommendationListDTO;
import toonpick.app.entity.RecommendationList;
import toonpick.app.entity.Webtoon;
import toonpick.app.exception.ResourceNotFoundException;
import toonpick.app.mapper.RecommendationListMapper;
import toonpick.app.repository.RecommendationListRepository;
import toonpick.app.repository.WebtoonRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationListService {

    private final RecommendationListRepository recommendationListRepository;
    private final WebtoonRepository webtoonRepository;
    private final RecommendationListMapper recommendationListMapper;

    public RecommendationListService(RecommendationListRepository recommendationListRepository, WebtoonRepository webtoonRepository, RecommendationListMapper recommendationListMapper) {
        this.recommendationListRepository = recommendationListRepository;
        this.webtoonRepository = webtoonRepository;
        this.recommendationListMapper = recommendationListMapper;
    }

    @Transactional
    public RecommendationListDTO createRecommendationList(RecommendationListDTO recommendationListDTO) {
        Set<Webtoon> webtoons = new HashSet<>(webtoonRepository.findAllById(
                recommendationListDTO.getWebtoonIds()));

        RecommendationList recommendationList = RecommendationList.builder()
                .theme(recommendationListDTO.getTheme())
                .description(recommendationListDTO.getDescription())
                .webtoons(webtoons)
                .build();

        recommendationList = recommendationListRepository.save(recommendationList);
        return recommendationListMapper.recommendationListToRecommendationListDTO(recommendationList);
    }

    @Transactional
    public RecommendationListDTO updateRecommendationList(Long id, RecommendationListDTO recommendationListDTO) {
        RecommendationList existingList = recommendationListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation List not found with id: " + id));

        Set<Webtoon> webtoons = new HashSet<>(webtoonRepository.findAllById(recommendationListDTO.getWebtoonIds()));

        existingList.update(recommendationListDTO.getTheme(), recommendationListDTO.getDescription(), webtoons);

        existingList = recommendationListRepository.save(existingList);
        return recommendationListMapper.recommendationListToRecommendationListDTO(existingList);
    }

    @Transactional
    public void deleteRecommendationList(Long id) {
        RecommendationList recommendationList = recommendationListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation List not found with id: " + id));
        recommendationListRepository.delete(recommendationList);
    }

    @Transactional(readOnly = true)
    public List<RecommendationListDTO> getAllRecommendationLists() {
        List<RecommendationList> recommendationLists = recommendationListRepository.findAll();
        return recommendationLists.stream()
                .map(recommendationListMapper::recommendationListToRecommendationListDTO)
                .collect(Collectors.toList());
    }
}
