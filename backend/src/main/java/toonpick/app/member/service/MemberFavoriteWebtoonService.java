package toonpick.app.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.webtoon.dto.WebtoonDTO;
import toonpick.app.member.entity.Member;
import toonpick.app.member.entity.MemberFavoriteWebtoon;
import toonpick.app.webtoon.entity.Webtoon;
import toonpick.app.exception.ResourceNotFoundException;
import toonpick.app.webtoon.mapper.WebtoonMapper;
import toonpick.app.member.repository.MemberFavoriteWebtoonRepository;
import toonpick.app.member.repository.MemberRepository;
import toonpick.app.webtoon.repository.WebtoonRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberFavoriteWebtoonService {

    private final MemberFavoriteWebtoonRepository favoriteRepository;
    private final WebtoonRepository webtoonRepository;
    private final MemberRepository memberRepository;
    private final WebtoonMapper webtoonMapper;

    public MemberFavoriteWebtoonService(MemberFavoriteWebtoonRepository favoriteRepository, WebtoonRepository webtoonRepository, MemberRepository memberRepository, WebtoonMapper webtoonMapper) {
        this.favoriteRepository = favoriteRepository;
        this.webtoonRepository = webtoonRepository;
        this.memberRepository = memberRepository;
        this.webtoonMapper = webtoonMapper;
    }

    @Transactional
    public void addFavoriteWebtoon(Long userId, Long webtoonId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found"));

        MemberFavoriteWebtoon favorite = MemberFavoriteWebtoon.builder()
                .member(member)
                .webtoon(webtoon)
                .addedDate(LocalDateTime.now())
                .build();

        favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavoriteWebtoon(Long userId, Long webtoonId) {
        MemberFavoriteWebtoon favorite = favoriteRepository.findByMemberIdAndWebtoonId(userId, webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));
        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public List<WebtoonDTO> getFavoriteWebtoons(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + userId));

        List<Webtoon> favoriteWebtoons = favoriteRepository.findByMember(member);

        return favoriteWebtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isFavoriteWebtoon(Long userId, Long webtoonId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + userId));

        return favoriteRepository.findByMemberIdAndWebtoonId(userId, webtoonId).isPresent();
    }

}
