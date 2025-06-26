import com.toonpick.domain.member.entity.Member;
import com.toonpick.domain.member.entity.ToonCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.toonpick.domain.webtoon.entity.Webtoon;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToonCollectionTest {

    private ToonCollection toonCollection;
    private Member member;
    private Webtoon webtoon1;
    private Webtoon webtoon2;

    @BeforeEach
    void setUp() {
        member = Member.builder().username("user1").build();
        webtoon1 = Webtoon.builder().id(1L).title("Webtoon 1").build();
        webtoon2 = Webtoon.builder().id(2L).title("Webtoon 2").build();

        toonCollection = ToonCollection.builder()
                .member(member)
                .title("My Toon Collection")
                .build();
    }

    @DisplayName("Webtoon 추가 테스트")
    @Test
    void testAddWebtoon() {
        // when
        toonCollection.addWebtoon(webtoon1);

        // then
        assertEquals(1, toonCollection.getWebtoons().size());
        assertTrue(toonCollection.getWebtoons().contains(webtoon1));
    }

    @DisplayName("Webtoon 제거 테스트")
    @Test
    void testRemoveWebtoon() {
        // given
        toonCollection.addWebtoon(webtoon1);
        toonCollection.addWebtoon(webtoon2);

        // when
        toonCollection.removeWebtoon(webtoon1);

        // then
        assertEquals(1, toonCollection.getWebtoons().size());
        assertFalse(toonCollection.getWebtoons().contains(webtoon1));
        assertTrue(toonCollection.getWebtoons().contains(webtoon2));
    }

    @DisplayName("Webtoon 리스트 초기화 테스트")
    @Test
    void testClearWebtoons() {
        // given
        toonCollection.addWebtoon(webtoon1);
        toonCollection.addWebtoon(webtoon2);

        // when
        toonCollection.clearWebtoons();

        // then
        assertEquals(0, toonCollection.getWebtoons().size());
    }

    @DisplayName("ToonCollection 제목 업데이트 테스트")
    @Test
    void testUpdateTitle() {
        // when
        toonCollection.updateTitle("Updated Collection Title");

        // then
        assertEquals("Updated Collection Title", toonCollection.getTitle());
    }

    @DisplayName("Webtoon 추가 후 컬렉션 변경 사항 테스트")
    @Test
    void testWebtoonListChangesAfterAdding() {
        // when
        toonCollection.addWebtoon(webtoon1);
        toonCollection.addWebtoon(webtoon2);

        // then
        List<Webtoon> webtoons = toonCollection.getWebtoons();
        assertEquals(2, webtoons.size());
        assertTrue(webtoons.contains(webtoon1));
        assertTrue(webtoons.contains(webtoon2));
    }
}
