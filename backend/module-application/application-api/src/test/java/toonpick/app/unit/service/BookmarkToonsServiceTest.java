package toonpick.app.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import toonpick.dto.WebtoonResponseDTO;
import toonpick.entity.BookmarkToons;
import toonpick.entity.Member;
import toonpick.entity.Webtoon;
import toonpick.exception.ResourceNotFoundException;
import toonpick.mapper.WebtoonMapper;
import toonpick.repository.BookmarkToonsRepository;
import toonpick.repository.MemberRepository;
import toonpick.repository.WebtoonRepository;
import toonpick.service.BookmarkToonsService;


import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class BookmarkToonsServiceTest {

    @Mock
    private BookmarkToonsRepository bookmarkToonsRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WebtoonRepository webtoonRepository;

    @Mock
    private WebtoonMapper webtoonMapper;

    @InjectMocks
    private BookmarkToonsService bookmarkToonsService;

    private Member member;
    private Webtoon webtoon;
    private BookmarkToons bookmarkToons;
    private WebtoonResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username("testuser")
                .build();

        webtoon = Webtoon.builder()
                .id(1L)
                .title("Test Webtoon")
                .build();

        bookmarkToons = BookmarkToons.builder()
                .member(member)
                .webtoon(webtoon)
                .build();

        responseDTO = WebtoonResponseDTO.builder()
                .id(1L)
                .title("Test Webtoon")
                .build();
    }

    @DisplayName("웹툰을 북마크 추가하는 단위 테스트")
    @Test
    void testAddBookmarkToons_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(bookmarkToonsRepository.save(any(BookmarkToons.class))).thenReturn(bookmarkToons);

        // when
        boolean result = bookmarkToonsService.addBookmarkToons("testuser", 1L);

        // then
        assertTrue(result);
        verify(memberRepository, times(1)).findByUsername("testuser");
        verify(webtoonRepository, times(1)).findById(1L);
        verify(bookmarkToonsRepository, times(1)).save(any(BookmarkToons.class));
    }

    @DisplayName("웹툰 북마크 추가시 회원이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testAddBookmarkToons_MemberNotFound() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> bookmarkToonsService.addBookmarkToons("testuser", 1L));
    }

    @DisplayName("웹툰 북마크 추가시 웹툰이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testAddBookmarkToons_WebtoonNotFound() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> bookmarkToonsService.addBookmarkToons("testuser", 1L));
    }

    @DisplayName("웹툰 북마크 삭제하는 단위 테스트")
    @Test
    void testRemoveBookmarkToons_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(bookmarkToonsRepository.findByMemberAndWebtoon(member, webtoon)).thenReturn(Optional.of(bookmarkToons));

        // when
        boolean result = bookmarkToonsService.removeBookmarkToons("testuser", 1L);

        // then
        assertTrue(result);
        verify(bookmarkToonsRepository, times(1)).delete(bookmarkToons);
    }

    @DisplayName("웹툰 북마크 삭제시 회원이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testRemoveBookmarkToons_MemberNotFound() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> bookmarkToonsService.removeBookmarkToons("testuser", 1L));
    }

    @DisplayName("웹툰 북마크 삭제시 웹툰이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testRemoveBookmarkToons_WebtoonNotFound() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> bookmarkToonsService.removeBookmarkToons("testuser", 1L));
    }

    @DisplayName("웹툰 북마크 조회하는 단위 테스트")
    @Test
    void testGetWebtoonsByUsername_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(bookmarkToonsRepository.findWebtoonsByMember(member)).thenReturn(List.of(webtoon));
        when(webtoonMapper.webtoonToWebtoonResponseDto(webtoon)).thenReturn(responseDTO);

        // when
        List<WebtoonResponseDTO> result = bookmarkToonsService.getWebtoonsByUsername("testuser");

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Webtoon", result.get(0).getTitle());
    }

    @DisplayName("웹툰 북마크 조회시 회원이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testGetWebtoonsByUsername_MemberNotFound() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> bookmarkToonsService.getWebtoonsByUsername("testuser"));
    }
}
