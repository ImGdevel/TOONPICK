import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.toonpick.entity.Author;
import com.toonpick.entity.Genre;
import com.toonpick.entity.Webtoon;
import com.toonpick.enums.AgeRating;
import com.toonpick.enums.Platform;
import com.toonpick.enums.SerializationStatus;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WebtoonTest {

    private Webtoon webtoon;
    private Set<Author> authors;
    private Set<Genre> genres;

    @BeforeEach
    void setUp() {
        authors = new HashSet<>();
        genres = new HashSet<>();

        authors.add(Author.builder().name("Author 1").build());
        genres.add(Genre.builder().name("Genre 1").build());

        webtoon = Webtoon.builder()
                .id(1L)
                .externalId("ext-123")
                .title("Sample Webtoon")
                .platform(Platform.NAVER)
                .dayOfWeek(DayOfWeek.MONDAY)
                .thumbnailUrl("http://example.com/thumbnail.jpg")
                .link("http://example.com/webtoon")
                .ageRating(AgeRating.ALL)
                .description("This is a sample webtoon.")
                .serializationStatus(SerializationStatus.COMPLETED)
                .episodeCount(10)
                .platformRating(4.5f)
                .publishStartDate(LocalDate.of(2023, 1, 1))
                .lastUpdatedDate(LocalDate.of(2023, 2, 1))
                .authors(authors)
                .genres(genres)
                .build();
    }

    @DisplayName("Webtoon 객체 생성 테스트")
    @Test
    void testWebtoonCreation() {
        assertNotNull(webtoon);
        assertEquals(1L, webtoon.getId());
        assertEquals("Sample Webtoon", webtoon.getTitle());
        assertEquals("ext-123", webtoon.getExternalId());
        assertEquals(Platform.NAVER, webtoon.getPlatform());
        assertEquals(DayOfWeek.MONDAY, webtoon.getDayOfWeek());
        assertEquals("http://example.com/webtoon", webtoon.getLink());
        assertEquals(AgeRating.ALL, webtoon.getAgeRating());
        assertEquals(SerializationStatus.COMPLETED, webtoon.getSerializationStatus());
        assertEquals(10, webtoon.getEpisodeCount());
        assertEquals(authors, webtoon.getAuthors());
        assertEquals(genres, webtoon.getGenres());
    }

    @DisplayName("에피소드 수와 최종 업데이트 날짜 업데이트 테스트")
    @Test
    void testUpdateEpisodeCountAndDate() {
        // when
        webtoon.updateEpisodeCountAndDate(15, LocalDate.of(2023, 3, 1));

        // then
        assertEquals(15, webtoon.getEpisodeCount());
        assertEquals(LocalDate.of(2023, 3, 1), webtoon.getLastUpdatedDate());
    }

    @DisplayName("웹툰 세부정보 업데이트 테스트")
    @Test
    void testUpdateWebtoonDetails() {
        // given
        Set<Author> newAuthors = new HashSet<>();
        newAuthors.add(Author.builder().name("Author 2").build());

        Set<Genre> newGenres = new HashSet<>();
        newGenres.add(Genre.builder().name("Genre 2").build());

        // when
        webtoon.updateWebtoonDetails(
                "Updated Webtoon",
                Platform.KAKAO,
                "Updated description.",
                SerializationStatus.COMPLETED,
                DayOfWeek.FRIDAY,
                "http://example.com/new-thumbnail.jpg",
                "http://example.com/new-webtoon",
                AgeRating.ADULT,
                newAuthors,
                newGenres
        );

        // then
        assertEquals("Updated Webtoon", webtoon.getTitle());
        assertEquals(Platform.KAKAO, webtoon.getPlatform());
        assertEquals("Updated description.", webtoon.getDescription());
        assertEquals(SerializationStatus.COMPLETED, webtoon.getSerializationStatus());
        assertEquals(DayOfWeek.FRIDAY, webtoon.getDayOfWeek());
        assertEquals("http://example.com/new-thumbnail.jpg", webtoon.getThumbnailUrl());
        assertEquals("http://example.com/new-webtoon", webtoon.getLink());
        assertEquals(AgeRating.ADULT, webtoon.getAgeRating());
        assertEquals(newAuthors, webtoon.getAuthors());
        assertEquals(newGenres, webtoon.getGenres());
    }

}
