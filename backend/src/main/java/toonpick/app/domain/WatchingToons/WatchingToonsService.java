package toonpick.app.domain.WatchingToons;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.exception.ResourceNotFoundException;
import toonpick.app.member.entity.Member;
import toonpick.app.member.repository.MemberRepository;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.repository.WebtoonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchingToonsService {

    private final WatchingToonsRepository watchingToonsRepository;
    private final MemberRepository memberRepository;
    private final WebtoonRepository webtoonRepository;

    @Transactional
    public boolean createWatchingToons(String username, Long webtoonId){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("Member not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(()-> new ResourceNotFoundException("Webtoon not found"));

        WatchingToons watchingToons = WatchingToons.builder()
                                        .member(member)
                                        .webtoon(webtoon)
                                        .build();

        watchingToonsRepository.save(watchingToons);
        return true;
    }

    @Transactional
    public boolean deleteWatchingToons(String username, Long webtoonId){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("Member not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(()-> new ResourceNotFoundException("Webtoon not found"));
        WatchingToons watchingToons = watchingToonsRepository.findByMemberAndWebtoon(member, webtoon)
                .orElseThrow(() -> new ResourceNotFoundException("WatchingToons not found"));

        watchingToonsRepository.delete(watchingToons);
        return true;
    }

    @Transactional(readOnly = true)
    public List<Webtoon> getWebtoonsByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("Member not found"));

        return watchingToonsRepository.findWebtoonsByMember(member);
    }

}
