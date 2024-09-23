package toonpick.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.dto.RecommendationListDTO;
import toonpick.app.dto.WebtoonDTO;
import toonpick.app.entity.RecommendationList;
import toonpick.app.entity.User;
import toonpick.app.entity.UserFavoriteWebtoon;
import toonpick.app.entity.Webtoon;
import toonpick.app.exception.ResourceNotFoundException;
import toonpick.app.mapper.WebtoonMapper;
import toonpick.app.repository.UserFavoriteWebtoonRepository;
import toonpick.app.repository.UserRepository;
import toonpick.app.repository.WebtoonRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFavoriteWebtoonService {

    private final UserFavoriteWebtoonRepository favoriteRepository;
    private final WebtoonRepository webtoonRepository;
    private final UserRepository userRepository;
    private final WebtoonMapper webtoonMapper;

    public UserFavoriteWebtoonService(UserFavoriteWebtoonRepository favoriteRepository, WebtoonRepository webtoonRepository, UserRepository userRepository, WebtoonMapper webtoonMapper) {
        this.favoriteRepository = favoriteRepository;
        this.webtoonRepository = webtoonRepository;
        this.userRepository = userRepository;
        this.webtoonMapper = webtoonMapper;
    }

    @Transactional
    public void addFavoriteWebtoon(Long userId, Long webtoonId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found"));

        UserFavoriteWebtoon favorite = UserFavoriteWebtoon.builder()
                .user(user)
                .webtoon(webtoon)
                .addedDate(LocalDateTime.now())
                .build();

        favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavoriteWebtoon(Long userId, Long webtoonId) {
        UserFavoriteWebtoon favorite = favoriteRepository.findByUserIdAndWebtoonId(userId, webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));
        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public List<WebtoonDTO> getFavoriteWebtoons(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Fetch favorite webtoons for the user
        List<Webtoon> favoriteWebtoons = favoriteRepository.findByUser(user);

        // Map Webtoon entities to WebtoonDTOs
        return favoriteWebtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

}
