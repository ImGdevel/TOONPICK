package com.toonpick.test.unit.webtoon.service;

import com.toonpick.entity.Webtoon;
import com.toonpick.entity.WebtoonAnalysisDataDocument;
import com.toonpick.enums.AgeRating;
import com.toonpick.enums.Platform;
import com.toonpick.enums.SerializationStatus;
import com.toonpick.exception.EntityNotFoundException;
import com.toonpick.repository.AuthorRepository;
import com.toonpick.repository.GenreRepository;
import com.toonpick.repository.WebtoonAnalysisRepository;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.webtoon.mapper.WebtoonAnalysisMapper;
import com.toonpick.webtoon.mapper.WebtoonMapper;
import com.toonpick.webtoon.response.WebtoonDetailsResponse;
import com.toonpick.webtoon.response.WebtoonResponse;
import com.toonpick.webtoon.service.WebtoonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class WebtoonServiceTest {

    @Mock
    private WebtoonRepository webtoonRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private WebtoonMapper webtoonMapper;

    @Mock
    private WebtoonAnalysisRepository analysisRepository;

    @Mock
    private WebtoonAnalysisMapper analysisMapper;

    @InjectMocks
    private WebtoonService webtoonService;

    private Webtoon webtoon;
    private WebtoonResponse responseDTO;
    private WebtoonDetailsResponse detailsResponse;
    private WebtoonAnalysisDataDocument analysisDocument;
    private WebtoonDetailsResponse.WebtoonAnalysisData analysisDto;

    @BeforeEach
    void setUp() {
        webtoon = Webtoon.builder()
                .id(1L)
                .title("Test Webtoon")
                .dayOfWeek(DayOfWeek.MONDAY)
                .thumbnailUrl("http://example.com/thumb.jpg")
                .ageRating(AgeRating.ADULT)
                .summary("Test Summary")
                .status(SerializationStatus.ONGOING)
                .publishStartDate(LocalDate.now())
                .lastUpdatedDate(LocalDate.now())
                .authors(new HashSet<>())
                .genres(new HashSet<>())
                .build();

        responseDTO = WebtoonResponse.builder()
                .id(1L)
                .title("Test Webtoon")
                .dayOfWeek(DayOfWeek.MONDAY)
                .thumbnailUrl("http://example.com/thumb.jpg")
                .link("http://example.com/webtoon")
                .ageRating(AgeRating.ADULT)
                .description("Test Description")
                .serializationStatus(SerializationStatus.ONGOING)
                .episodeCount(10)
                .build();

        detailsResponse = WebtoonDetailsResponse.builder()
                .id(1L)
                .title("Test Webtoon")
                .build();

        analysisDocument = WebtoonAnalysisDataDocument.builder()
                .webtoonId(1L)
                .totalViews(1000)
                .build();

        analysisDto = new WebtoonDetailsResponse.WebtoonAnalysisData();
        analysisDto.totalViews = 1000;
    }

    @DisplayName("Id로 웹툰 조회 유닛 테스트")
    @Test
    void testGetWebtoon_Success() {
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(webtoonMapper.toWebtoonResponse(webtoon)).thenReturn(responseDTO);

        WebtoonResponse result = webtoonService.getWebtoon(1L);

        assertNotNull(result);
        assertEquals("Test Webtoon", result.getTitle());
    }

    @DisplayName("Id로 웹툰 조회 예외 유닛 테스트")
    @Test
    void testGetWebtoon_NotFound() {
        when(webtoonRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> webtoonService.getWebtoon(2L));
    }

    @DisplayName("웹툰 상세 조회 유닛 테스트")
    @Test
    void testGetWebtoonDetails_Success() {
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(webtoonMapper.toWebtoonDetailsResponse(webtoon)).thenReturn(detailsResponse);
        when(analysisRepository.findByWebtoonId(1L)).thenReturn(Optional.of(analysisDocument));
        when(analysisMapper.toDto(analysisDocument)).thenReturn(analysisDto);

        WebtoonDetailsResponse result = webtoonService.getWebtoonDetails(1L);

        assertNotNull(result);
        assertEquals(1000, result.getAnalysisData().totalViews);
    }
}
