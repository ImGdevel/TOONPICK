package toonpick.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.domain.watching_toons.WatchingToons;
import toonpick.app.exception.ErrorCode;
import toonpick.app.exception.exception.ResourceNotFoundException;
import toonpick.app.domain.member.Member;
import toonpick.app.repository.MemberRepository;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.repository.WatchingToonsRepository;
import toonpick.app.repository.WebtoonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchingToonsService {

    private final WatchingToonsRepository watchingToonsRepository;
    private final MemberRepository memberRepository;
    private final WebtoonRepository webtoonRepository;

    @Transactional
    public boolean addWatchingToons(String username, Long webtoonId){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, webtoonId));

        WatchingToons watchingToons = WatchingToons.builder()
                                        .member(member)
                                        .webtoon(webtoon)
                                        .build();

        watchingToonsRepository.save(watchingToons);
        return true;
    }

    @Transactional
    public boolean removeWatchingToons(String username, Long webtoonId){
        // todo : 클라이언트와 상의 후 id 로 변경, 성능 최적화 고려
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, webtoonId));
        WatchingToons watchingToons = watchingToonsRepository.findByMemberAndWebtoon(member, webtoon)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WATCHING_TOON_NOT_FOUND));

        watchingToonsRepository.delete(watchingToons);
        return true;
    }

    @Transactional(readOnly = true)
    public List<Webtoon> getWebtoonsByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));

        return watchingToonsRepository.findWebtoonsByMember(member);
    }

}
