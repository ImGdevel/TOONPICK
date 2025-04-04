package unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.toonpick.dto.WebtoonCreateRequestDTO;
import com.toonpick.dto.WebtoonRequestDTO;
import com.toonpick.dto.WebtoonResponseDTO;
import com.toonpick.entity.Webtoon;
import com.toonpick.enums.AgeRating;
import com.toonpick.enums.Platform;
import com.toonpick.enums.SerializationStatus;
import com.toonpick.exception.ResourceAlreadyExistsException;
import com.toonpick.exception.ResourceNotFoundException;

import com.toonpick.mapper.WebtoonMapper;
import com.toonpick.repository.AuthorRepository;
import com.toonpick.repository.GenreRepository;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.service.WebtoonService;


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

    @InjectMocks
    private WebtoonService webtoonService;

    private Webtoon webtoon;
    private WebtoonCreateRequestDTO createRequestDTO;
    private WebtoonResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        webtoon = Webtoon.builder()
                .id(1L)
                .externalId("ext123")
                .title("Test Webtoon")
                .platform(Platform.NAVER)
                .dayOfWeek(DayOfWeek.MONDAY)
                .thumbnailUrl("http://example.com/thumb.jpg")
                .link("http://example.com/webtoon")
                .ageRating(AgeRating.ADULT)
                .description("Test Description")
                .serializationStatus(SerializationStatus.ONGOING)
                .episodeCount(10)
                .platformRating(4.5f)
                .publishStartDate(LocalDate.now())
                .lastUpdatedDate(LocalDate.now())
                .authors(new HashSet<>())
                .genres(new HashSet<>())
                .build();

        createRequestDTO = WebtoonCreateRequestDTO.builder()
                .externalId("ext123")
                .title("Test Webtoon")
                .platform(Platform.NAVER)
                .dayOfWeek(DayOfWeek.MONDAY)
                .thumbnailUrl("http://example.com/thumb.jpg")
                .link("http://example.com/webtoon")
                .ageRating(AgeRating.ADULT)
                .description("Test Description")
                .serializationStatus(SerializationStatus.ONGOING)
                .episodeCount(10)
                .platformRating(4.5f)
                .publishStartDate(LocalDate.now())
                .lastUpdatedDate(LocalDate.now())
                .authors(new HashSet<>())
                .genres(new HashSet<>())
                .build();

        responseDTO = WebtoonResponseDTO.builder()
                .id(1L)
                .title("Test Webtoon")
                .platform(Platform.NAVER)
                .dayOfWeek(DayOfWeek.MONDAY)
                .thumbnailUrl("http://example.com/thumb.jpg")
                .link("http://example.com/webtoon")
                .ageRating(AgeRating.ADULT)
                .description("Test Description")
                .serializationStatus(SerializationStatus.ONGOING)
                .episodeCount(10)
                .platformRating(4.5f)
                .build();
    }

    @DisplayName("웹툰 생성 유닛 테스트")
    @Test
    void testCreateWebtoon_Success() {
        when(webtoonRepository.findByExternalId(createRequestDTO.getExternalId())).thenReturn(Optional.empty());
        when(webtoonRepository.save(any(Webtoon.class))).thenReturn(webtoon);
        when(webtoonMapper.webtoonToWebtoonResponseDto(webtoon)).thenReturn(responseDTO);

        // when
        WebtoonResponseDTO result = webtoonService.createWebtoon(createRequestDTO);

        // then
        assertNotNull(result);
        assertEquals("Test Webtoon", result.getTitle());
        verify(webtoonRepository, times(1)).findByExternalId(createRequestDTO.getExternalId());
        verify(webtoonRepository, times(1)).save(any(Webtoon.class));
        verify(webtoonMapper, times(1)).webtoonToWebtoonResponseDto(webtoon);
    }

    @DisplayName("웹툰 중복 생성 예외 유닛 테스트")
    @Test
    void testCreateWebtoon_AlreadyExists() {
        // given
        when(webtoonRepository.findByExternalId(createRequestDTO.getExternalId())).thenReturn(Optional.of(webtoon));

        // when & then
        assertThrows(ResourceAlreadyExistsException.class, () -> webtoonService.createWebtoon(createRequestDTO));
    }

    @DisplayName("Id로 웹툰 조회 유닛 테스트")
    @Test
    void testGetWebtoonById_Success() {
        // given
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(webtoonMapper.webtoonToWebtoonResponseDto(webtoon)).thenReturn(responseDTO);

        // when
        WebtoonResponseDTO result = webtoonService.getWebtoonById(1L);

        // then
        assertNotNull(result);
        assertEquals("Test Webtoon", result.getTitle());
    }

    @DisplayName("Id로 웹툰 조회 예외 유닛 테스트")
    @Test
    void testGetWebtoonById_NotFound() {
        // given
        when(webtoonRepository.findById(2L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> webtoonService.getWebtoonById(2L));
    }

    @DisplayName("웹툰 업데이트 유닛 테스트")
    @Test
    void testUpdateWebtoon_Success() {
        // given
        WebtoonRequestDTO updateRequestDTO = WebtoonRequestDTO.builder()
                .title("Updated Webtoon")
                .platform(Platform.KAKAO)
                .build();

        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(authorRepository.findAllById(null)).thenReturn(new ArrayList<>());
        when(genreRepository.findAllById(null)).thenReturn(new ArrayList<>());
        when(webtoonRepository.save(any(Webtoon.class))).thenReturn(webtoon);
        when(webtoonMapper.webtoonToWebtoonResponseDto(webtoon)).thenReturn(responseDTO);

        // when
        WebtoonResponseDTO result = webtoonService.updateWebtoon(1L, updateRequestDTO);

        // then
        assertNotNull(result);
        assertEquals("Test Webtoon", result.getTitle());
    }

    @DisplayName("웹툰 삭제 유닛 테스트")
    @Test
    void testDeleteWebtoon_Success() {
        // given
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));

        // when
        webtoonService.deleteWebtoon(1L);

        // then
        verify(webtoonRepository, times(1)).delete(webtoon);
    }

    @DisplayName("웹툰 삭제 예외 유닛 테스트")
    @Test
    void testDeleteWebtoon_NotFound() {
        // given
        when(webtoonRepository.findById(2L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> webtoonService.deleteWebtoon(2L));
    }
}
