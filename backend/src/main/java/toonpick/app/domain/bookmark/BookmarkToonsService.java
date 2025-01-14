package toonpick.app.domain.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.common.exception.ResourceNotFoundException;
import toonpick.app.member.entity.Member;
import toonpick.app.member.repository.MemberRepository;
import toonpick.app.webtoon.entity.Webtoon;
import toonpick.app.webtoon.repository.WebtoonRepository;

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
