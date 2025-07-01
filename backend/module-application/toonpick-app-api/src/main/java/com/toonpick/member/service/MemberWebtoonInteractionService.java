package com.toonpick.member.service;

import com.toonpick.domain.member.entity.Member;
import com.toonpick.domain.member.entity.MemberWebtoonInteraction;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.member.enums.WatchingStatus;
import com.toonpick.common.exception.EntityNotFoundException;
import com.toonpick.member.request.LastReadUpdateRequest;
import com.toonpick.member.request.WebtoonInteractionResponse;
import com.toonpick.domain.member.repository.MemberRepository;
import com.toonpick.domain.member.repository.MemberWebtoonInteractionRepository;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.common.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberWebtoonInteractionService {

    private final MemberRepository memberRepository;
    private final WebtoonRepository webtoonRepository;
    private final MemberWebtoonInteractionRepository interactionRepository;

    /**
     * 사용자가 웹툰을 좋아요 표시
     */
    public void markAsLiked(Long webtoonId, String username) {
        MemberWebtoonInteraction interaction = loadInteraction(webtoonId, username);
        interaction.like();
    }

    /**
     * 사용자가 웹툰 좋아요 해제
     */
    public void cancelLike(Long webtoonId, String username) {
        MemberWebtoonInteraction interaction = loadInteraction(webtoonId, username);
        interaction.unlike();
    }

    /**
     * 사용자가 웹툰을 북마크에 추가
     */
    public void addToBookmarks(Long webtoonId, String username) {
        MemberWebtoonInteraction interaction = loadInteraction(webtoonId, username);
        interaction.bookmark();
    }

    /**
     * 사용자가 웹툰 북마크 해제
     */
    public void removeFromBookmarks(Long webtoonId, String username) {
        MemberWebtoonInteraction interaction = loadInteraction(webtoonId, username);
        interaction.unbookmark();
    }

    /**
     * 사용자의 시청 상태(WatchingStatus) 업데이트
     */
    public void changeWatchingStatus(Long webtoonId, WatchingStatus status, String username) {
        MemberWebtoonInteraction interaction = loadInteraction(webtoonId, username);
        interaction.updateStatus(status);
    }

    /**
     * 사용자의 마지막 시청 에피소드 및 시각 업데이트
     */
    public void updateLastReadingProgress(Long webtoonId, LastReadUpdateRequest request, String username) {
        MemberWebtoonInteraction interaction = loadInteraction(webtoonId, username);
        interaction.updateReading(request.getEpisode(), request.getTimestamp());
    }

    /**
     * 알림 설정 여부 저장
     */
    public void updateNotificationSetting(Long webtoonId, Boolean enabled, String username) {
        MemberWebtoonInteraction interaction = loadInteraction(webtoonId, username);
        interaction.setNotification(enabled);
    }

    /**
     * 사용자의 해당 웹툰에 대한 상호작용 정보 조회
     */
    @Transactional(readOnly = true)
    public WebtoonInteractionResponse getInteraction(Long webtoonId, String username) {
        Member member = getMember(username);
        Webtoon webtoon = getWebtoon(webtoonId);

        return interactionRepository.findByMemberAndWebtoon(member, webtoon)
                .map(WebtoonInteractionResponse::from)
                .orElse(WebtoonInteractionResponse.empty());
    }

    /**
     * 공통 로직 추출: 상호작용 조회 or 생성
     */
    private MemberWebtoonInteraction loadInteraction(Long webtoonId, String username) {
        Member member = getMember(username);
        Webtoon webtoon = getWebtoon(webtoonId);
        return interactionRepository.findByMemberAndWebtoon(member, webtoon)
                .orElseGet(() -> interactionRepository.save(
                        MemberWebtoonInteraction.builder()
                                .member(member)
                                .webtoon(webtoon)
                                .build()
                ));
    }

    private Member getMember(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Webtoon getWebtoon(Long webtoonId) {
        return webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.WEBTOON_NOT_FOUND));
    }
}
