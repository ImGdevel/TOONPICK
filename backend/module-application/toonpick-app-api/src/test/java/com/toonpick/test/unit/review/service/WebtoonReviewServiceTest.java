package com.toonpick.test.unit.review.service;

import com.toonpick.dto.PagedResponseDTO;
import com.toonpick.entity.Member;
import com.toonpick.entity.ReviewLike;
import com.toonpick.entity.Webtoon;
import com.toonpick.entity.WebtoonReview;
import com.toonpick.repository.MemberRepository;
import com.toonpick.repository.ReviewLikeRepository;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.repository.WebtoonReviewRepository;
import com.toonpick.review.request.WebtoonReviewCreateDTO;
import com.toonpick.review.response.WebtoonReviewDTO;
import com.toonpick.review.service.WebtoonReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class WebtoonReviewServiceTest {

    @Mock
    private WebtoonReviewRepository webtoonReviewRepository;
    @Mock
    private ReviewLikeRepository reviewLikeRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private WebtoonRepository webtoonRepository;
    @InjectMocks
    private WebtoonReviewService webtoonReviewService;

    private Member member;
    private Webtoon webtoon;
    private WebtoonReview review;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username("testuser")
                .build();
        webtoon = Webtoon.builder()
                .id(1L)
                .title("Test Webtoon")
                .build();
        review = WebtoonReview.builder()
                .member(member)
                .webtoon(webtoon)
                .comment("Great webtoon!")
                .rating(5.0f)
                .likes(0)
                .build();
    }


    @DisplayName("리뷰 생성 성공 유닛 테스트")
    @Test
    void createReview_Success() {
        // given
        WebtoonReviewCreateDTO reviewCreateDTO = WebtoonReviewCreateDTO.builder()
                .webtoonId(1L)
                .rating(4.5f)
                .comment("Good")
                .build();

        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(webtoonReviewRepository.save(any(WebtoonReview.class))).thenReturn(review);

        // when
        WebtoonReviewDTO result = webtoonReviewService.createReview(reviewCreateDTO, "testuser");

        // then
        assertNotNull(result);
        assertEquals(review.getComment(), result.getComment());
        assertEquals(review.getRating(), result.getRating());
        verify(webtoonRepository, times(1)).addReview(webtoon.getId(), reviewCreateDTO.getRating());
    }

    @DisplayName("리뷰 가져오기 성공 유닛 테스트")
    @Test
    void getReview_Success() {
        // given
        when(webtoonReviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // when
        WebtoonReviewDTO result = webtoonReviewService.getReview(1L);

        // then
        assertNotNull(result);
        assertEquals(review.getComment(), result.getComment());
        assertEquals(review.getRating(), result.getRating());
    }

    @DisplayName("리뷰 수정 성공 유닛 테스트")
    @Test
    void updateReview_Success() {
        // given
        WebtoonReviewCreateDTO updateDTO = WebtoonReviewCreateDTO.builder()
                .webtoonId(1L)
                .comment("Updated comment")
                .rating(5.0f)
                .build();

        when(webtoonReviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(webtoonReviewRepository.save(any(WebtoonReview.class))).thenReturn(review);

        // when
        WebtoonReviewDTO result = webtoonReviewService.updateReview(1L, updateDTO);

        // then
        assertNotNull(result);
        assertEquals(updateDTO.getComment(), result.getComment());
        assertEquals(updateDTO.getRating(), result.getRating());
        verify(webtoonRepository, times(1)).updateReview(webtoon.getId(), review.getRating(), updateDTO.getRating());
    }

    @DisplayName("리뷰 삭제 성공 유닛 테스트")
    @Test
    void deleteReview_Success() {
        // given
        when(webtoonReviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // when
        webtoonReviewService.deleteReview(1L);

        // then
        verify(webtoonRepository, times(1)).removeReview(webtoon.getId(), review.getRating());
        verify(webtoonReviewRepository, times(1)).delete(review);
    }

    @DisplayName("리뷰 좋아요 토글 유닛 테스트")
    @Test
    void toggleLike_Success() throws Exception {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonReviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewLikeRepository.findByMemberAndReview(member, review)).thenReturn(Optional.empty());

        // when
        CompletableFuture<Boolean> result = webtoonReviewService.toggleLike("testuser", 1L);

        // then
        assertTrue(result.get());
        verify(reviewLikeRepository, times(1)).save(any(ReviewLike.class));
        verify(webtoonReviewRepository, times(1)).incrementLikes(1L);
    }

    @DisplayName("특정 유저의 리뷰 조회 성공 유닛 테스트")
    @Test
    void getUserReviewForWebtoon_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(webtoonReviewRepository.findWebtoonReviewByMemberAndWebtoon(member, webtoon)).thenReturn(Optional.of(review));

        // when
        Optional<WebtoonReviewDTO> result = webtoonReviewService.getUserReviewForWebtoon("testuser", 1L);

        // then
        assertTrue(result.isPresent());
        assertEquals(review.getComment(), result.get().getComment());
    }


    @DisplayName("웹툰 리뷰 리스트 조회 - 로그인하지 않은 유저일 때 성공")
    @Test
    void getReviewsByWebtoon_WithoutLogin_Success() {
        // given
        Webtoon webtoon = Webtoon.builder().id(1L).title("웹툰").build();
        Member member = Member.builder().username("작성자").build();
        WebtoonReview review = WebtoonReview.builder().id(200L).member(member).webtoon(webtoon).comment("꿀잼").likes(10).build();

        Page<WebtoonReview> page = new PageImpl<>(List.of(review));

        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(webtoonReviewRepository.findByWebtoon(eq(webtoon), any(Pageable.class))).thenReturn(page);

        // when
        PagedResponseDTO<WebtoonReviewDTO> result = webtoonReviewService.getReviewsByWebtoon(
                1L, "latest", 0, 10, null); // username null

        // then
        assertNotNull(result);
        assertEquals(1, result.getData().size());

        WebtoonReviewDTO dto = result.getData().get(0);
        assertEquals("꿀잼", dto.getComment());
        assertFalse(dto.getIsLiked());
    }



    @DisplayName("웹툰 리뷰 리스트 조회 - 로그인한 유저일 때 성공")
    @Test
    void getReviewsByWebtoon_WithLogin_Success() {
        // given
        Member member = Member.builder().id(1L).username("testUser").build();
        Webtoon webtoon = Webtoon.builder().id(1L).title("웹툰").build();
        WebtoonReview review = WebtoonReview.builder().id(100L).member(member).webtoon(webtoon).comment("재밌음").likes(3).build();

        Page<WebtoonReview> page = new PageImpl<>(List.of(review));
        List<Long> likedReviewIds = List.of(100L); // 유저가 이 리뷰에 좋아요 함

        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(memberRepository.findByUsername("testUser")).thenReturn(Optional.of(member));
        when(webtoonReviewRepository.findByWebtoon(eq(webtoon), any(Pageable.class))).thenReturn(page);
        when(reviewLikeRepository.findLikedReviewIdsByMemberIdAndReviewIds(1L, List.of(100L)))
                .thenReturn(likedReviewIds);

        // when
        PagedResponseDTO<WebtoonReviewDTO> result = webtoonReviewService.getReviewsByWebtoon(
                1L, "best", 0, 10, "testUser");

        // then
        assertNotNull(result);
        assertEquals(1, result.getData().size());

        WebtoonReviewDTO dto = result.getData().get(0);
        assertEquals("재밌음", dto.getComment());
        assertTrue(dto.getIsLiked()); // 좋아요 체크됨
    }

}
