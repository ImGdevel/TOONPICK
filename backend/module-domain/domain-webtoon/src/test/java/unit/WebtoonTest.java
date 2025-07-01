package unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.toonpick.domain.webtoon.entity.Author;
import com.toonpick.domain.webtoon.entity.Genre;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.enums.AgeRating;
import com.toonpick.domain.webtoon.enums.SerializationStatus;


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
                .title("Sample Webtoon")
                .dayOfWeek(DayOfWeek.MONDAY)
                .thumbnailUrl("http://example.com/thumbnail.jpg")
                .ageRating(AgeRating.ALL)
                .status(SerializationStatus.COMPLETED)
                .summary("This is a sample webtoon.")
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
        assertEquals(DayOfWeek.MONDAY, webtoon.getDayOfWeek());
        assertEquals(AgeRating.ALL, webtoon.getAgeRating());
        assertEquals(SerializationStatus.COMPLETED, webtoon.getSerializationStatus());
        assertEquals(authors, webtoon.getAuthors());
        assertEquals(genres, webtoon.getGenres());
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
                "Updated description.",
                SerializationStatus.COMPLETED,
                DayOfWeek.FRIDAY,
                "http://example.com/new-thumbnail.jpg",
                AgeRating.ADULT,
                newAuthors,
                newGenres
        );

        // then
        assertEquals("Updated Webtoon", webtoon.getTitle());
        assertEquals("Updated description.", webtoon.getSummary());
        assertEquals(SerializationStatus.COMPLETED, webtoon.getSerializationStatus());
        assertEquals(DayOfWeek.FRIDAY, webtoon.getDayOfWeek());
        assertEquals("http://example.com/new-thumbnail.jpg", webtoon.getThumbnailUrl());
        assertEquals(AgeRating.ADULT, webtoon.getAgeRating());
        assertEquals(newAuthors, webtoon.getAuthors());
        assertEquals(newGenres, webtoon.getGenres());
    }

}
