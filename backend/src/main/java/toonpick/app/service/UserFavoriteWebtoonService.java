package toonpick.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.entity.User;
import toonpick.app.entity.UserFavoriteWebtoon;
import toonpick.app.entity.Webtoon;
import toonpick.app.exception.ResourceNotFoundException;
import toonpick.app.repository.UserFavoriteWebtoonRepository;
import toonpick.app.repository.UserRepository;
import toonpick.app.repository.WebtoonRepository;

import java.time.LocalDateTime;

@Service
public class UserFavoriteWebtoonService {

    private final UserFavoriteWebtoonRepository favoriteRepository;
    private final WebtoonRepository webtoonRepository;
    private final UserRepository userRepository;

    public UserFavoriteWebtoonService(UserFavoriteWebtoonRepository favoriteRepository, WebtoonRepository webtoonRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.webtoonRepository = webtoonRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addFavorite(Long userId, Long webtoonId) {
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
    public void removeFavorite(Long userId, Long webtoonId) {
        UserFavoriteWebtoon favorite = favoriteRepository.findByUserIdAndWebtoonId(userId, webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));
        favoriteRepository.delete(favorite);
    }

}
