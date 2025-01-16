package toonpick.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.domain.bookmark_toons.BookmarkToons;
import toonpick.app.repository.BookmarkToonsRepository;
import toonpick.app.exception.ResourceNotFoundException;
import toonpick.app.domain.member.Member;
import toonpick.app.repository.MemberRepository;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.repository.WebtoonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkToonsService {

    private final BookmarkToonsRepository bookmarkToonsRepository;
    private final MemberRepository memberRepository;
    private final WebtoonRepository webtoonRepository;

    @Transactional
    public boolean createBookmarkToons(String username, Long webtoonId){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("Member not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(()-> new ResourceNotFoundException("Webtoon not found"));

        BookmarkToons bookmarkToons = BookmarkToons.builder()
                                        .member(member)
                                        .webtoon(webtoon)
                                        .build();

        bookmarkToonsRepository.save(bookmarkToons);
        return true;
    }

    @Transactional
    public boolean deleteBookmarkToons(String username, Long webtoonId){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("Member not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(()-> new ResourceNotFoundException("Webtoon not found"));
        BookmarkToons bookmarkToons = bookmarkToonsRepository.findByMemberAndWebtoon(member, webtoon)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmarks not found"));

        bookmarkToonsRepository.delete(bookmarkToons);
        return true;
    }

    @Transactional(readOnly = true)
    public List<Webtoon> getWebtoonsByUsername(String username) {
         Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("Member not found"));

         return bookmarkToonsRepository.findWebtoonsByMember(member);
    }

}
