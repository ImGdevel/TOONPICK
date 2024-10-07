package toonpick.app;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import toonpick.app.dto.WebtoonReviewCreateDTO;
import toonpick.app.dto.WebtoonReviewDTO;
import toonpick.app.entity.User;
import toonpick.app.entity.Webtoon;
import toonpick.app.entity.WebtoonReview;
import toonpick.app.entity.enums.AgeRating;
import toonpick.app.entity.enums.Platform;
import toonpick.app.entity.enums.SerializationStatus;
import toonpick.app.repository.ReviewLikeRepository;
import toonpick.app.repository.UserRepository;
import toonpick.app.repository.WebtoonRepository;
import toonpick.app.repository.WebtoonReviewRepository;
import toonpick.app.service.WebtoonReviewService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

public class WebtoonReviewServiceTest {

    @Mock
    private WebtoonReviewRepository webtoonReviewRepository;

    @Mock
    private ReviewLikeRepository reviewLikeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebtoonRepository webtoonRepository;

    @InjectMocks
    private WebtoonReviewService webtoonReviewService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReview_Success() {
        // Given
        Long userId = 1L;
        Long webtoonId = 1L;
        WebtoonReviewCreateDTO reviewCreateDTO = WebtoonReviewCreateDTO.builder()
                .userId(userId)
                .webtoonId(webtoonId)
                .rating(4.5f)
                .comment("Great webtoon!")
                .build();

        User user = User.builder()
                .username("testuser")
                .profilePicture("profile.jpg")
                .accountCreationDate(LocalDate.now())
                .build();

        Webtoon webtoon = Webtoon.builder()
                .id(webtoonId)
                .title("Test Webtoon")
                .platform(Platform.NAVER)
                .platformId("naver123")
                .averageRating(4.5f)
                .platformRating(4.7f)
                .description("A great webtoon.")
                .serializationStatus(SerializationStatus.연재)
                .episodeCount(10)
                .serializationStartDate(LocalDate.now().minusDays(30))
                .lastUpdatedDate(LocalDate.now())
                .week(DayOfWeek.MONDAY)
                .thumbnailUrl("thumbnail.jpg")
                .url("http://webtoon.com")
                .ageRating(AgeRating.AGE_12)
                .build();

        WebtoonReview savedReview = WebtoonReview.builder()
                .user(user)
                .webtoon(webtoon)
                .rating(4.5f)
                .comment("Great webtoon!")
                .likes(0)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(webtoonRepository.findById(webtoonId)).thenReturn(Optional.of(webtoon));
        when(webtoonReviewRepository.save(any(WebtoonReview.class))).thenReturn(savedReview);

        // When
        WebtoonReviewDTO result = webtoonReviewService.createReview(reviewCreateDTO);

        // Then
        assertThat(result).isNotNull(); // Check if the result is not null
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getWebtoonId()).isEqualTo(webtoonId);
        assertThat(result.getLikes()).isEqualTo(0);
        verify(webtoonReviewRepository, times(1)).save(any(WebtoonReview.class));
    }

        @Test
    public void testCreateReview_UserNotFound() {
        // Given
        Long userId = 1L;
        Long webtoonId = 1L;
        WebtoonReviewCreateDTO reviewCreateDTO = WebtoonReviewCreateDTO.builder()
                .userId(userId)
                .webtoonId(webtoonId)
                .rating(4.5f)
                .comment("Great webtoon!")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> webtoonReviewService.createReview(reviewCreateDTO));

        // Then
        assertThat(thrown).isInstanceOf(RuntimeException.class).hasMessageContaining("User not found");
        verify(webtoonReviewRepository, never()).save(any());
    }

    @Test
    public void testCreateReview_WebtoonNotFound() {
        // Given
        Long userId = 1L;
        Long webtoonId = 1L;
        WebtoonReviewCreateDTO reviewCreateDTO = WebtoonReviewCreateDTO.builder()
                .userId(userId)
                .webtoonId(webtoonId)
                .rating(4.5f)
                .comment("Great webtoon!")
                .build();

        User user = User.builder().username("testuser").build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(webtoonRepository.findById(webtoonId)).thenReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> webtoonReviewService.createReview(reviewCreateDTO));

        // Then
        assertThat(thrown).isInstanceOf(RuntimeException.class).hasMessageContaining("Webtoon not found");
        verify(webtoonReviewRepository, never()).save(any());
    }

    @Test
    public void testCreateReview_InvalidRating() {
        // Given
        Long userId = 1L;
        Long webtoonId = 1L;
        WebtoonReviewCreateDTO reviewCreateDTO = WebtoonReviewCreateDTO.builder()
                .userId(userId)
                .webtoonId(webtoonId)
                .rating(6.0f) // 유효하지 않은 평점
                .comment("Great webtoon!")
                .build();

        User user = User.builder().username("testuser").build();
        Webtoon webtoon = Webtoon.builder().id(webtoonId).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(webtoonRepository.findById(webtoonId)).thenReturn(Optional.of(webtoon));

        // When
        Throwable thrown = catchThrowable(() -> webtoonReviewService.createReview(reviewCreateDTO));

        // Then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Invalid rating");
        verify(webtoonReviewRepository, never()).save(any());
    }

    // 추가적인 테스트 케이스 작성...
}
