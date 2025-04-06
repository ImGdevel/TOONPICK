package com.toonpick.member_enagement.service;

import com.toonpick.entity.Member;
import com.toonpick.repository.MemberRepository;
import com.toonpick.webtoon.mapper.WebtoonMapper;
import com.toonpick.webtoon.response.WebtoonResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toonpick.entity.BookmarkToons;
import com.toonpick.entity.Webtoon;
import com.toonpick.repository.BookmarkToonsRepository;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.type.ErrorCode;

import com.toonpick.exception.ResourceNotFoundException;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkToonsService {

    private final BookmarkToonsRepository bookmarkToonsRepository;
    private final MemberRepository memberRepository;
    private final WebtoonRepository webtoonRepository;
    private final WebtoonMapper webtoonMapper;

    @Transactional
    public boolean addBookmarkToons(String username, Long webtoonId){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, webtoonId));

        BookmarkToons bookmarkToons = BookmarkToons.builder()
                                        .member(member)
                                        .webtoon(webtoon)
                                        .build();

        bookmarkToonsRepository.save(bookmarkToons);
        return true;
    }

    @Transactional
    public boolean removeBookmarkToons(String username, Long webtoonId){
         // todo : 클라이언트와 상의 후 id 로 변경, 성능 최적화 고려
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, webtoonId));
        BookmarkToons bookmarkToons = bookmarkToonsRepository.findByMemberAndWebtoon(member, webtoon)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKMARK_TOON_NOT_FOUND));

        bookmarkToonsRepository.delete(bookmarkToons);
        return true;
    }

    @Transactional(readOnly = true)
    public List<WebtoonResponseDTO> getWebtoonsByUsername(String username) {
         Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
         List<Webtoon> webtoons = bookmarkToonsRepository.findWebtoonsByMember(member);
         return webtoons.stream()
                 .map(webtoonMapper::webtoonToWebtoonResponseDto)
                 .collect(Collectors.toList());
    }

}
