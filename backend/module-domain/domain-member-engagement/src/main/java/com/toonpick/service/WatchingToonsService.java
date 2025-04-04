package com.toonpick.service;

import com.toonpick.entity.Member;
import com.toonpick.entity.WatchingToons;
import com.toonpick.repository.MemberRepository;
import com.toonpick.repository.WatchingToonsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toonpick.dto.WebtoonResponseDTO;
import com.toonpick.entity.Webtoon;
import com.toonpick.mapper.WebtoonMapper;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.type.ErrorCode;
import com.toonpick.exception.ResourceNotFoundException;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchingToonsService {

    private final WatchingToonsRepository watchingToonsRepository;
    private final MemberRepository memberRepository;
    private final WebtoonRepository webtoonRepository;
    private final WebtoonMapper webtoonMapper;

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
    public List<WebtoonResponseDTO> getWebtoonsByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));

        List<Webtoon> webtoons = watchingToonsRepository.findWebtoonsByMember(member);

        return webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonResponseDto)
                .collect(Collectors.toList());
    }

}
