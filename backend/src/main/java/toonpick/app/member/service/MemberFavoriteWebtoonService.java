package toonpick.app.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.webtoon.dto.WebtoonDTO;
import toonpick.app.member.entity.Member;
import toonpick.app.member.entity.MemberFavoriteWebtoon;
import toonpick.app.webtoon.entity.Webtoon;
import toonpick.app.common.exception.ResourceNotFoundException;
import toonpick.app.webtoon.mapper.WebtoonMapper;
import toonpick.app.member.repository.MemberFavoriteWebtoonRepository;
import toonpick.app.member.repository.MemberRepository;
import toonpick.app.webtoon.repository.WebtoonRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberFavoriteWebtoonService {

    private final MemberFavoriteWebtoonRepository favoriteRepository;
    private final WebtoonRepository webtoonRepository;
    private final MemberRepository memberRepository;
    private final WebtoonMapper webtoonMapper;


    @Transactional
    public void addFavoriteWebtoon(String username, Long webtoonId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found"));

        // 중복 확인
        if (favoriteRepository.findByMemberAndWebtoon(member, webtoon).isPresent()) {
            throw new IllegalArgumentException("This webtoon is already added to favorites.");
        }

        MemberFavoriteWebtoon favorite = MemberFavoriteWebtoon.builder()
                .member(member)
                .webtoon(webtoon)
                .addedDate(LocalDateTime.now())
                .build();

        favoriteRepository.save(favorite);
    }


    @Transactional
    public void removeFavoriteWebtoon(String username, Long webtoonId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found"));
        MemberFavoriteWebtoon favorite = favoriteRepository.findByMemberAndWebtoon(member, webtoon)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public List<WebtoonDTO> getFavoriteWebtoons(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with username: " + username));

        List<Webtoon> favoriteWebtoons = favoriteRepository.findByMember(member);

        return favoriteWebtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isFavoriteWebtoon(String username, Long webtoonId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with username: " + username));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found"));

        return favoriteRepository.findByMemberAndWebtoon(member, webtoon).isPresent();
    }


}
