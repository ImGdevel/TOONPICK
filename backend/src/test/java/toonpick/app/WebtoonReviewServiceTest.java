package toonpick.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.dto.WebtoonReviewCreateDTO;
import toonpick.app.dto.WebtoonReviewDTO;
import toonpick.app.entity.User;
import toonpick.app.entity.Webtoon;
import toonpick.app.entity.WebtoonReview;
import toonpick.app.entity.enums.Platform;
import toonpick.app.entity.enums.SerializationStatus;
import toonpick.app.exception.ResourceNotFoundException;
import toonpick.app.repository.UserRepository;
import toonpick.app.repository.WebtoonRepository;
import toonpick.app.repository.WebtoonReviewRepository;
import toonpick.app.service.WebtoonReviewService;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class WebtoonReviewServiceTest {

    @Autowired
    private WebtoonReviewService webtoonReviewService;

    @Autowired
    private WebtoonReviewRepository webtoonReviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebtoonRepository webtoonRepository;

    private User testUser;
    private Webtoon testWebtoon;

    @BeforeEach
    public void setup() {
        testUser = userRepository.save(User.builder()
                .username("testUser" + System.currentTimeMillis())
                .profilePicture("default.png")
                .accountCreationDate(LocalDate.now())
                .build());

        testWebtoon = webtoonRepository.save(Webtoon.builder()
                .title("Test Webtoon")
                .platform(Platform.NAVER)
                .platformId("naver-123-" + System.currentTimeMillis())
                .averageRating(4.5f)
                .description("Test webtoon description")
                .serializationStatus(SerializationStatus.연재)
                .episodeCount(10)
                .serializationStartDate(LocalDate.now())
                .lastUpdatedDate(LocalDate.now())
                .week(DayOfWeek.MONDAY)
                .url("http://testwebtoon.com")
                .build());

    }

    @Test
    public void testCreateAndDeleteReview() {
        WebtoonReviewCreateDTO reviewCreateDTO = WebtoonReviewCreateDTO.builder()
                .rating(5.0f)
                .comment("Great webtoon!")
                .build();

        WebtoonReviewDTO reviewDTO = webtoonReviewService.createReview(reviewCreateDTO, testWebtoon.getId(), testUser.getId());
        assertNotNull(reviewDTO);
        assertEquals("Great webtoon!", reviewDTO.getComment());

        webtoonReviewService.deleteReview(reviewDTO.getId());
        assertThrows(ResourceNotFoundException.class, () -> webtoonReviewService.getReview(reviewDTO.getId()));
    }

    @Test
    public void testToggleLike() {
        WebtoonReviewCreateDTO reviewCreateDTO = WebtoonReviewCreateDTO.builder()
                .rating(4.0f)
                .comment("Not bad")
                .build();

        WebtoonReviewDTO reviewDTO = webtoonReviewService.createReview(reviewCreateDTO, testWebtoon.getId(), testUser.getId());

        Boolean liked = webtoonReviewService.toggleLike(testUser.getId(), reviewDTO.getId());
        assertTrue(liked);

        liked = webtoonReviewService.toggleLike(testUser.getId(), reviewDTO.getId());
        assertFalse(liked);
    }

    @Test
    public void testMultipleLikeBySameUser() {
        WebtoonReviewCreateDTO reviewCreateDTO = WebtoonReviewCreateDTO.builder()
                .rating(4.5f)
                .comment("Pretty good!")
                .build();

        WebtoonReviewDTO reviewDTO = webtoonReviewService.createReview(reviewCreateDTO, testWebtoon.getId(), testUser.getId());

        for (int i = 0; i < 10; i++) {
            webtoonReviewService.toggleLike(testUser.getId(), reviewDTO.getId());
        }

        WebtoonReview updatedReview = webtoonReviewRepository.findById(reviewDTO.getId()).orElseThrow();
        assertEquals(0, updatedReview.getLikes());  // 짝수번 누르면 다시 0
    }

    @Test
    public void testMultipleUsersLikeReview() {
        User user2 = userRepository.save(User.builder()
                .username("user2")
                .profilePicture("default2.png")
                .accountCreationDate(LocalDate.now())
                .build());

        WebtoonReviewCreateDTO reviewCreateDTO = WebtoonReviewCreateDTO.builder()
                .rating(3.5f)
                .comment("Interesting!")
                .build();

        WebtoonReviewDTO reviewDTO = webtoonReviewService.createReview(reviewCreateDTO, testWebtoon.getId(), testUser.getId());

        webtoonReviewService.toggleLike(testUser.getId(), reviewDTO.getId());
        webtoonReviewService.toggleLike(user2.getId(), reviewDTO.getId());

        WebtoonReview updatedReview = webtoonReviewRepository.findById(reviewDTO.getId()).orElseThrow();
        assertEquals(2, updatedReview.getLikes());  // 두 명이 좋아요 눌렀으니 2
    }

    @Test
    public void testConcurrentLikeAndUnlike() throws InterruptedException {
        User user2 = userRepository.save(User.builder()
                .username("user2")
                .profilePicture("default2.png")
                .accountCreationDate(LocalDate.now())
                .build());

        WebtoonReviewCreateDTO reviewCreateDTO = WebtoonReviewCreateDTO.builder()
                .rating(5.0f)
                .comment("Concurrent test review")
                .build();

        WebtoonReviewDTO reviewDTO = webtoonReviewService.createReview(reviewCreateDTO, testWebtoon.getId(), testUser.getId());

        Runnable likeTask = () -> webtoonReviewService.toggleLike(testUser.getId(), reviewDTO.getId());
        Runnable unlikeTask = () -> webtoonReviewService.toggleLike(user2.getId(), reviewDTO.getId());

        Thread thread1 = new Thread(likeTask);
        Thread thread2 = new Thread(unlikeTask);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        WebtoonReview updatedReview = webtoonReviewRepository.findById(reviewDTO.getId()).orElseThrow();
        System.out.println("최종 좋아요 수: " + updatedReview.getLikes());
    }
}
