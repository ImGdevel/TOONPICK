package com.toonpick.member_enagement.service;

import com.toonpick.entity.FavoriteToon;
import com.toonpick.entity.Member;
import com.toonpick.repository.FavoriteToonRepository;
import com.toonpick.repository.MemberRepository;
import com.toonpick.webtoon.mapper.WebtoonMapper;
import com.toonpick.webtoon.response.WebtoonResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toonpick.entity.Webtoon;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.type.ErrorCode;
import com.toonpick.exception.ResourceAlreadyExistsException;
import com.toonpick.exception.ResourceNotFoundException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteToonService {

    private final FavoriteToonRepository favoriteRepository;
    private final WebtoonRepository webtoonRepository;
    private final MemberRepository memberRepository;
    private final WebtoonMapper webtoonMapper;


    @Transactional
    public void addFavoriteWebtoon(String username, Long webtoonId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, webtoonId));

        // 중복 확인
        if (favoriteRepository.findByMemberAndWebtoon(member, webtoon).isPresent()) {
            throw new ResourceAlreadyExistsException(ErrorCode.FAVORITE_TOON_ALREADY_EXISTS);
        }

        FavoriteToon favorite = FavoriteToon.builder()
                .member(member)
                .webtoon(webtoon)
                .addedDate(LocalDateTime.now())
                .build();

        favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavoriteWebtoon(String username, Long webtoonId) {
        // todo : 클라이언트와 상의 후 id 로 변경, 성능 최적화 고려
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, webtoonId));
        FavoriteToon favorite = favoriteRepository.findByMemberAndWebtoon(member, webtoon)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FAVORITE_TOON_NOT_FOUND));

        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public List<WebtoonResponseDTO> getFavoriteWebtoons(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));

        List<Webtoon> favoriteWebtoons = favoriteRepository.findFavoriteWebtoonsByMember(member);

        return favoriteWebtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isFavoriteWebtoon(String username, Long webtoonId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, webtoonId));

        return favoriteRepository.findByMemberAndWebtoon(member, webtoon).isPresent();
    }


}
