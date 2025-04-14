package com.toonpick.test.unit.member_engagement.service;

import com.toonpick.entity.Member;
import com.toonpick.entity.Webtoon;
import com.toonpick.member_enagement.service.WatchingToonsService;
import com.toonpick.repository.MemberRepository;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.webtoon.mapper.WebtoonMapper;
import com.toonpick.webtoon.response.WebtoonResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.toonpick.exception.ResourceNotFoundException;


import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class WatchingToonsServiceTest {

    @Mock
    private WatchingToonsRepository watchingToonsRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WebtoonRepository webtoonRepository;

    @Mock
    private WebtoonMapper webtoonMapper;

    @InjectMocks
    private WatchingToonsService watchingToonsService;

    private Member member;
    private Webtoon webtoon;
    private WatchingToons watchingToons;
    private WebtoonResponse responseDTO;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username("testuser")
                .build();

        webtoon = Webtoon.builder()
                .id(1L)
                .title("Test Webtoon")
                .build();

        watchingToons = WatchingToons.builder()
                .member(member)
                .webtoon(webtoon)
                .build();

        responseDTO = WebtoonResponse.builder()
                .id(1L)
                .title("Test Webtoon")
                .build();
    }

    @DisplayName("웹툰을 추가하는 단위 테스트")
    @Test
    void testAddWatchingToons_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(watchingToonsRepository.save(any(WatchingToons.class))).thenReturn(watchingToons);

        // when
        boolean result = watchingToonsService.addWatchingToons("testuser", 1L);

        // then
        assertTrue(result);
        verify(memberRepository, times(1)).findByUsername("testuser");
        verify(webtoonRepository, times(1)).findById(1L);
        verify(watchingToonsRepository, times(1)).save(any(WatchingToons.class));
    }


    @DisplayName("웹툰 추가시 회원이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testAddWatchingToons_MemberNotFound() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> watchingToonsService.addWatchingToons("testuser", 1L));
    }

    @DisplayName("웹툰 추가시 웹툰이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testAddWatchingToons_WebtoonNotFound() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> watchingToonsService.addWatchingToons("testuser", 1L));
    }

    @DisplayName("웹툰을 삭제하는 단위 테스트")
    @Test
    void testRemoveWatchingToons_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(watchingToonsRepository.findByMemberAndWebtoon(member, webtoon)).thenReturn(Optional.of(watchingToons));

        // when
        boolean result = watchingToonsService.removeWatchingToons("testuser", 1L);

        // then
        assertTrue(result);
        verify(watchingToonsRepository, times(1)).delete(watchingToons);
    }

    @DisplayName("웹툰 삭제시 회원이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testRemoveWatchingToons_MemberNotFound() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> watchingToonsService.removeWatchingToons("testuser", 1L));
    }

    @DisplayName("웹툰 삭제시 웹툰이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testRemoveWatchingToons_WebtoonNotFound() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> watchingToonsService.removeWatchingToons("testuser", 1L));
    }

    @DisplayName("웹툰을 조회하는 단위 테스트")
    @Test
    void testGetWebtoonsByUsername_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(watchingToonsRepository.findWebtoonsByMember(member)).thenReturn(List.of(webtoon));
        when(webtoonMapper.webtoonToWebtoonResponseDto(webtoon)).thenReturn(responseDTO);

        // when
        List<WebtoonResponse> result = watchingToonsService.getWebtoonsByUsername("testuser");

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Webtoon", result.get(0).getTitle());
    }

    @DisplayName("웹툰 조회시 회원이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testGetWebtoonsByUsername_MemberNotFound() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> watchingToonsService.getWebtoonsByUsername("testuser"));
    }
}
