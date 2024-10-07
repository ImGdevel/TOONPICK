package toonpick.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import toonpick.app.dto.WebtoonReviewDTO;
import toonpick.app.entity.User;
import toonpick.app.entity.WebtoonReview;
import toonpick.app.entity.Webtoon;
import toonpick.app.mapper.WebtoonReviewMapper;
import toonpick.app.repository.WebtoonReviewRepository;
import toonpick.app.repository.UserRepository;
import toonpick.app.repository.WebtoonRepository;
import toonpick.app.service.WebtoonReviewService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebtoonReviewServiceTest {

    @Mock
    private WebtoonReviewRepository webtoonReviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WebtoonRepository webtoonRepository;
    @Mock
    private WebtoonReviewMapper userRatingMapper;

    @InjectMocks
    private WebtoonReviewService webtoonReviewService;

    private User dummyUser1;
    private User dummyUser2;
    private Webtoon dummyWebtoon;
    private WebtoonReview dummyRating1;
    private WebtoonReview dummyRating2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dummyUser1 = User.builder().username("user1").build();
        dummyUser2 = User.builder().username("user2").build();
        dummyWebtoon = Webtoon.builder().title("Dummy Webtoon").build();

        dummyRating1 = WebtoonReview.builder()
                .user(dummyUser1)
                .webtoon(dummyWebtoon)
                .rating(4.5f)
                .comment("Great!")
                .likes(10)
                .modifyDate(LocalDate.now())
                .build();

        dummyRating2 = WebtoonReview.builder()
                .user(dummyUser2)
                .webtoon(dummyWebtoon)
                .rating(5.0f)
                .comment("Amazing!")
                .likes(15)
                .modifyDate(LocalDate.now())
                .build();
    }

    // 1. 하나의 더미 웹툰에 대하여 더미 유저 여러명이 웹툰 평가를 작성하는 테스트
    @Test
    void createMultipleRatingsForWebtoon() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(dummyUser1));
        when(webtoonRepository.findById(anyLong())).thenReturn(Optional.of(dummyWebtoon));
        when(webtoonReviewRepository.save(any(WebtoonReview.class))).thenReturn(dummyRating1);

        WebtoonReviewDTO ratingDTO = webtoonReviewService.createUserRating(1L, 1L, 4.5f, "Great!");
        assertEquals("Great!", ratingDTO.getComment());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(dummyUser2));
        when(webtoonReviewRepository.save(any(WebtoonReview.class))).thenReturn(dummyRating2);

        WebtoonReviewDTO ratingDTO2 = webtoonReviewService.createUserRating(2L, 1L, 5.0f, "Amazing!");
        assertEquals("Amazing!", ratingDTO2.getComment());
    }

    // 2. 동시에 여러명이 작성하기도 하는 테스트
    @Test
    void concurrentRatingSubmission() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(dummyUser1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(dummyUser2));
        when(webtoonRepository.findById(anyLong())).thenReturn(Optional.of(dummyWebtoon));
        when(webtoonReviewRepository.save(any(WebtoonReview.class))).thenReturn(dummyRating1).thenReturn(dummyRating2);

        Runnable user1Task = () -> webtoonReviewService.createUserRating(1L, 1L, 4.5f, "Good!");
        Runnable user2Task = () -> webtoonReviewService.createUserRating(2L, 1L, 5.0f, "Excellent!");

        Thread user1Thread = new Thread(user1Task);
        Thread user2Thread = new Thread(user2Task);

        user1Thread.start();
        user2Thread.start();

        assertDoesNotThrow(() -> {
            user1Thread.join();
            user2Thread.join();
        });
    }

    /*
    // 3. 작성하고 업로드 하는 도중 누군가 최신순 웹툰 코멘트를 요청하는 테스트
    @Test
    void fetchLatestCommentsWhileSubmitting() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(dummyUser1));
        when(webtoonRepository.findById(anyLong())).thenReturn(Optional.of(dummyWebtoon));
        when(webtoonReviewRepository.save(any(WebtoonReview.class))).thenReturn(dummyRating1);

        Pageable pageable = PageRequest.of(0, 20);
        Page<WebtoonReview> ratingPage = mock(Page.class);
        when(webtoonReviewRepository.findByWebtoonIdOrderByModifyDateDesc(1L, pageable)).thenReturn(ratingPage);

        webtoonReviewService.createUserRating(1L, 1L, 4.5f, "Good!");
        webtoonReviewService.getRatingsByLatest(1L, 0);

        verify(webtoonReviewRepository).findByWebtoonIdOrderByModifyDateDesc(1L, pageable);
    }
    */


    // 4. likes 테스트를 위한 추가 메서드 구현 및 테스트
    @Test
    void testLikeFeature() {
        when(webtoonReviewRepository.findById(1L)).thenReturn(Optional.of(dummyRating1));
        webtoonReviewService.updateLikes(1L, 12);
        assertEquals(12, dummyRating1.getLikes());
    }

    // 5. 여러명의 유저가 likes 증가 및 감소 테스트
    @Test
    void multipleUsersIncreaseDecreaseLikes() {
        when(webtoonReviewRepository.findById(1L)).thenReturn(Optional.of(dummyRating1));
        webtoonReviewService.updateLikes(1L, 12);  // 유저 1이 좋아요를 12로 증가
        assertEquals(12, dummyRating1.getLikes());

        webtoonReviewService.updateLikes(1L, 8);   // 유저 2가 좋아요를 8로 감소
        assertEquals(8, dummyRating1.getLikes());
    }
}
