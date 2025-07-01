package com.toonpick.test.unit.member.service;

import com.toonpick.domain.member.entity.Member;
import com.toonpick.domain.member.entity.MemberWebtoonInteraction;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.member.enums.WatchingStatus;
import com.toonpick.member.request.LastReadUpdateRequest;
import com.toonpick.member.request.WebtoonInteractionResponse;
import com.toonpick.member.service.MemberWebtoonInteractionService;
import com.toonpick.domain.member.repository.MemberRepository;
import com.toonpick.domain.member.repository.MemberWebtoonInteractionRepository;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class MemberWebtoonInteractionServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WebtoonRepository webtoonRepository;

    @Mock
    private MemberWebtoonInteractionRepository interactionRepository;

    @InjectMocks
    private MemberWebtoonInteractionService interactionService;

    private Member member;
    private Webtoon webtoon;
    private MemberWebtoonInteraction interaction;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username("testuser")
                .build();

        webtoon = Webtoon.builder()
                .id(1L)
                .build();

        interaction = MemberWebtoonInteraction.builder()
                .member(member)
                .webtoon(webtoon)
                .status(WatchingStatus.NONE)
                .build();
    }

    @Test
    @DisplayName("웹툰 좋아요 표시 테스트")
    void testMarkAsLiked() {
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(interactionRepository.findByMemberAndWebtoon(member, webtoon)).thenReturn(Optional.of(interaction));

        interactionService.markAsLiked(1L, "testuser");

        assertTrue(interaction.isLiked());
    }

    @Test
    @DisplayName("웹툰 좋아요 해제 테스트")
    void testCancelLike() {
        interaction.like(); // 먼저 좋아요 상태로 설정
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(interactionRepository.findByMemberAndWebtoon(member, webtoon)).thenReturn(Optional.of(interaction));

        interactionService.cancelLike(1L, "testuser");

        assertFalse(interaction.isLiked());
    }

    @Test
    @DisplayName("웹툰 북마크 추가 테스트")
    void testAddToBookmarks() {
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(interactionRepository.findByMemberAndWebtoon(member, webtoon)).thenReturn(Optional.of(interaction));

        interactionService.addToBookmarks(1L, "testuser");

        assertTrue(interaction.isBookmarked());
    }

    @Test
    @DisplayName("웹툰 북마크 해제 테스트")
    void testRemoveFromBookmarks() {
        interaction.bookmark(); // 먼저 북마크 상태로 설정
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(interactionRepository.findByMemberAndWebtoon(member, webtoon)).thenReturn(Optional.of(interaction));

        interactionService.removeFromBookmarks(1L, "testuser");

        assertFalse(interaction.isBookmarked());
    }

    @Test
    @DisplayName("시청 상태 변경 테스트")
    void testChangeWatchingStatus() {
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(interactionRepository.findByMemberAndWebtoon(member, webtoon)).thenReturn(Optional.of(interaction));

        interactionService.changeWatchingStatus(1L, WatchingStatus.WATCHING, "testuser");

        assertEquals(WatchingStatus.WATCHING, interaction.getStatus());
    }

    @Test
    @DisplayName("마지막 시청 에피소드 및 시각 업데이트 테스트")
    void testUpdateLastReadingProgress() {
        LastReadUpdateRequest request = new LastReadUpdateRequest();
        request.setEpisode(5);
        request.setTimestamp(LocalDateTime.now());

        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(interactionRepository.findByMemberAndWebtoon(member, webtoon)).thenReturn(Optional.of(interaction));

        interactionService.updateLastReadingProgress(1L, request, "testuser");

        assertEquals(5, interaction.getLastReadEpisode());
        assertEquals(request.getTimestamp(), interaction.getLastReadAt());
    }

    @Test
    @DisplayName("알림 설정 업데이트 테스트")
    void testUpdateNotificationSetting() {
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(interactionRepository.findByMemberAndWebtoon(member, webtoon)).thenReturn(Optional.of(interaction));

        interactionService.updateNotificationSetting(1L, true, "testuser");

        assertTrue(interaction.isNotificationEnabled());
    }

    @Test
    @DisplayName("상호작용 정보 조회 테스트")
    void testGetInteraction() {
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(interactionRepository.findByMemberAndWebtoon(member, webtoon)).thenReturn(Optional.of(interaction));


        WebtoonInteractionResponse response = interactionService.getInteraction(1L, "testuser");

        assertNotNull(response);
    }

    @Test
    @DisplayName("상호작용 정보 조회 - 존재하지 않을 경우 기본 응답 반환 테스트")
    void testGetInteraction_NotFound() {
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(interactionRepository.findByMemberAndWebtoon(member, webtoon)).thenReturn(Optional.empty());

        WebtoonInteractionResponse response = interactionService.getInteraction(1L, "testuser");

        assertNotNull(response);

        assertEquals(response.isLiked(), false);
        assertEquals(response.isBookmarked(), false);
        assertEquals(response.isNotificationEnabled(), false);
        assertEquals(response.getStatus(), WatchingStatus.NONE);
        assertEquals(response.getLastReadEpisode(), 0);
        assertNull(response.getLastReadAt());
    }

}
