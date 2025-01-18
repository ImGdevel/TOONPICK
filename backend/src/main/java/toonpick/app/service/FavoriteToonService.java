package toonpick.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.domain.favoritetoon.FavoriteToon;
import toonpick.app.domain.member.Member;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.dto.webtoon.WebtoonResponseDTO;
import toonpick.app.exception.ErrorCode;
import toonpick.app.exception.exception.ResourceAlreadyExistsException;
import toonpick.app.exception.exception.ResourceNotFoundException;
import toonpick.app.mapper.WebtoonMapper;
import toonpick.app.repository.FavoriteToonRepository;
import toonpick.app.repository.MemberRepository;
import toonpick.app.repository.WebtoonRepository;

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
