package toonpick.app.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.review.dto.WebtoonReviewCreateDTO;
import toonpick.app.review.dto.WebtoonReviewDTO;
import toonpick.app.member.entity.Member;
import toonpick.app.webtoon.entity.Webtoon;
import toonpick.app.review.entity.WebtoonReview;
import toonpick.app.webtoon.entity.enums.Platform;
import toonpick.app.webtoon.entity.enums.SerializationStatus;
import toonpick.app.util.exception.ResourceNotFoundException;
import toonpick.app.member.repository.MemberRepository;
import toonpick.app.webtoon.repository.WebtoonRepository;
import toonpick.app.review.repository.WebtoonReviewRepository;
import toonpick.app.review.service.WebtoonReviewService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

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
    private MemberRepository memberRepository;

    @Autowired
    private WebtoonRepository webtoonRepository;

    private Member testMember;
    private Webtoon testWebtoon;

    @BeforeEach
    public void setup() {
        testMember = memberRepository.save(Member.builder()
                .username("testMember" + System.currentTimeMillis())
                .profilePicture("default.png")
                .build());

        testWebtoon = webtoonRepository.save(Webtoon.builder()
                .title("Test Webtoon")
                .platform(Platform.NAVER)
                .platformId("naver-123-" + System.currentTimeMillis())
                .averageRating(0f)
                .description("Test webtoon description")
                .serializationStatus(SerializationStatus.ONGOING)
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

        WebtoonReviewDTO reviewDTO = webtoonReviewService.createReview(reviewCreateDTO, testWebtoon.getId(), testMember.getId());
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

        WebtoonReviewDTO reviewDTO = webtoonReviewService.createReview(reviewCreateDTO, testWebtoon.getId(), testMember.getId());

        // 첫 번째 좋아요 클릭
        CompletableFuture<Boolean> likedFuture = webtoonReviewService.toggleLike(testMember.getId(), reviewDTO.getId());
        Boolean liked = likedFuture.join();
        assertTrue(liked);

        // 두 번째 좋아요 클릭 (취소)
        likedFuture = webtoonReviewService.toggleLike(testMember.getId(), reviewDTO.getId());
        liked = likedFuture.join();
        assertFalse(liked);
    }

    @Test
    public void testMultipleLikeBySameUser() {
        WebtoonReviewCreateDTO reviewCreateDTO = WebtoonReviewCreateDTO.builder()
                .rating(4.5f)
                .comment("Pretty good!")
                .build();

        WebtoonReviewDTO reviewDTO = webtoonReviewService.createReview(reviewCreateDTO, testWebtoon.getId(), testMember.getId());

        for (int i = 0; i < 10; i++) {
            webtoonReviewService.toggleLike(testMember.getId(), reviewDTO.getId());
        }

        WebtoonReview updatedReview = webtoonReviewRepository.findById(reviewDTO.getId()).orElseThrow();
        assertEquals(0, updatedReview.getLikes());  // 짝수번 누르면 다시 0
    }

    @Test
    public void testMultipleUsersLikeReview() throws InterruptedException {
        Member member2 = memberRepository.save(Member.builder()
                .username("member2")
                .profilePicture("default2.png")
                .build());

        WebtoonReviewCreateDTO reviewCreateDTO = WebtoonReviewCreateDTO.builder()
                .rating(3.5f)
                .comment("Interesting!")
                .build();

        WebtoonReviewDTO reviewDTO = webtoonReviewService.createReview(reviewCreateDTO, testWebtoon.getId(), testMember.getId());

        webtoonReviewService.toggleLike(testMember.getId(), reviewDTO.getId());
        webtoonReviewService.toggleLike(member2.getId(), reviewDTO.getId());


        Thread.sleep(10); // 비동기적 업데이트 됨을 고려

        WebtoonReview updatedReview = webtoonReviewRepository.findById(reviewDTO.getId()).orElseThrow();
        assertEquals(2, updatedReview.getLikes());
    }

    @Test
    public void testConcurrentLikeAndUnlike() throws InterruptedException {
        Member member2 = memberRepository.save(Member.builder()
                .username("member2")
                .profilePicture("default2.png")
                .build());

        WebtoonReviewCreateDTO reviewCreateDTO = WebtoonReviewCreateDTO.builder()
                .rating(5.0f)
                .comment("Concurrent test review")
                .build();

        WebtoonReviewDTO reviewDTO = webtoonReviewService.createReview(reviewCreateDTO, testWebtoon.getId(), testMember.getId());

        Runnable likeTask = () -> webtoonReviewService.toggleLike(testMember.getId(), reviewDTO.getId());
        Runnable unlikeTask = () -> webtoonReviewService.toggleLike(member2.getId(), reviewDTO.getId());

        Thread thread1 = new Thread(likeTask);
        Thread thread2 = new Thread(unlikeTask);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        WebtoonReview updatedReview = webtoonReviewRepository.findById(reviewDTO.getId()).orElseThrow();
        assertEquals(0, updatedReview.getLikes());
    }

    @Test
    public void testMultipleUsersReviewAverageRating() {
        Member memberX = memberRepository.save(Member.builder()
                .username("memberX")
                .profilePicture("defaultX.png")
                .build());

        Member memberY = memberRepository.save(Member.builder()
                .username("memberY")
                .profilePicture("defaultY.png")
                .build());

        Member memberZ = memberRepository.save(Member.builder()
                .username("memberZ")
                .profilePicture("defaultZ.png")
                .build());

        WebtoonReviewCreateDTO reviewX = WebtoonReviewCreateDTO.builder()
                .rating(5.0f)
                .comment("Review by X")
                .build();
        webtoonReviewService.createReview(reviewX, testWebtoon.getId(), memberX.getId());
        Webtoon updatedWebtoonX = webtoonRepository.findById(testWebtoon.getId()).orElseThrow();
        System.out.println("평균 평점 after X's review: " + updatedWebtoonX.getAverageRating());

        WebtoonReviewCreateDTO reviewY = WebtoonReviewCreateDTO.builder()
                .rating(4.0f)
                .comment("Review by Y")
                .build();
        webtoonReviewService.createReview(reviewY, testWebtoon.getId(), memberY.getId());
        Webtoon updatedWebtoonY = webtoonRepository.findById(testWebtoon.getId()).orElseThrow();
        System.out.println("평균 평점 after Y's review: " + updatedWebtoonY.getAverageRating());

        WebtoonReviewCreateDTO reviewZ = WebtoonReviewCreateDTO.builder()
                .rating(1.0f)
                .comment("Review by Z")
                .build();
        webtoonReviewService.createReview(reviewZ, testWebtoon.getId(), memberZ.getId());
        Webtoon updatedWebtoonZ = webtoonRepository.findById(testWebtoon.getId()).orElseThrow();
        System.out.println("평균 평점 after Z's review: " + updatedWebtoonZ.getAverageRating());

        assertEquals(3.33f, updatedWebtoonZ.getAverageRating(), 0.01);
    }

}
