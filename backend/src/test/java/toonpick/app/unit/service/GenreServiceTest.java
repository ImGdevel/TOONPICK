package toonpick.app.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import toonpick.app.domain.webtoon.Genre;
import toonpick.app.dto.webtoon.GenreDTO;
import toonpick.app.exception.exception.ResourceAlreadyExistsException;
import toonpick.app.exception.exception.ResourceNotFoundException;
import toonpick.app.mapper.GenreMapper;
import toonpick.app.repository.GenreRepository;
import toonpick.app.service.GenreService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @InjectMocks
    private GenreService genreService;

    private Genre genre;
    private GenreDTO genreDTO;

    @BeforeEach
    void setUp() {
        genre = Genre.builder()
                .name("Action")
                .build();

        genreDTO = GenreDTO.builder()
                .id(1L)
                .name("Action")
                .build();
    }

    @DisplayName("모든 장르를 조회하는 단위 테스트")
    @Test
    void testGetAllGenres() {
        // given
        given(genreRepository.findAll()).willReturn(List.of(genre));
        given(genreMapper.genreToGenreDto(genre)).willReturn(genreDTO);

        // when
        List<GenreDTO> result = genreService.getAllGenres();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Action", result.get(0).getName());
        verify(genreRepository, times(1)).findAll();
        verify(genreMapper, times(1)).genreToGenreDto(genre);
    }

    @DisplayName("ID로 장르를 조회하는 단위 테스트")
    @Test
    void testGetGenreById_Success() {
        // given
        given(genreRepository.findById(1L)).willReturn(Optional.of(genre));
        given(genreMapper.genreToGenreDto(genre)).willReturn(genreDTO);

        // when
        GenreDTO result = genreService.getGenre(1L);

        // then
        assertNotNull(result);
        assertEquals("Action", result.getName());
        verify(genreRepository, times(1)).findById(1L);
        verify(genreMapper, times(1)).genreToGenreDto(genre);
    }

    @DisplayName("ID로 장르를 조회 시 존재하지 않으면 예외 발생")
    @Test
    void testGetGenreById_NotFound() {
        // given
        given(genreRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> genreService.getGenre(1L));
        verify(genreRepository, times(1)).findById(1L);
    }

    @DisplayName("새로운 장르를 생성하는 단위 테스트")
    @Test
    void testCreateGenre_Success() {
        // given
        given(genreRepository.existsByName("Action")).willReturn(false);
        given(genreMapper.genreDtoToGenre(genreDTO)).willReturn(genre);
        given(genreRepository.save(genre)).willReturn(genre);
        given(genreMapper.genreToGenreDto(genre)).willReturn(genreDTO);

        // when
        GenreDTO result = genreService.createGenre(genreDTO);

        // then
        assertNotNull(result);
        assertEquals("Action", result.getName());
        verify(genreRepository, times(1)).existsByName("Action");
        verify(genreRepository, times(1)).save(genre);
        verify(genreMapper, times(1)).genreToGenreDto(genre);
    }

    @DisplayName("이미 존재하는 장르를 생성하려고 할 때 예외 발생")
    @Test
    void testCreateGenre_AlreadyExists() {
        // given
        given(genreRepository.existsByName("Action")).willReturn(true);

        // when & then
        assertThrows(ResourceAlreadyExistsException.class, () -> genreService.createGenre(genreDTO));
        verify(genreRepository, times(1)).existsByName("Action");
    }

    @DisplayName("장르를 생성하거나 찾는 단위 테스트")
    @Test
    void testFindOrCreateGenre_WhenNotExists() {
        // given
        given(genreRepository.findByName("Action")).willReturn(Optional.empty());
        given(genreMapper.genreDtoToGenre(genreDTO)).willReturn(genre);
        given(genreRepository.save(genre)).willReturn(genre);
        given(genreMapper.genreToGenreDto(genre)).willReturn(genreDTO);

        // when
        GenreDTO result = genreService.findOrCreateGenre(genreDTO);

        // then
        assertNotNull(result);
        assertEquals("Action", result.getName());
        verify(genreRepository, times(1)).findByName("Action");
        verify(genreRepository, times(1)).save(genre);
        verify(genreMapper, times(1)).genreToGenreDto(genre);
    }

    @DisplayName("장르를 삭제하는 단위 테스트")
    @Test
    void testDeleteGenre_Success() {
        // given
        given(genreRepository.findById(1L)).willReturn(Optional.of(genre));

        // when
        genreService.deleteGenre(1L);

        // then
        verify(genreRepository, times(1)).findById(1L);
        verify(genreRepository, times(1)).delete(genre);
    }

    @DisplayName("삭제하려는 장르가 존재하지 않을 때 예외 발생")
    @Test
    void testDeleteGenre_NotFound() {
        // given
        given(genreRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> genreService.deleteGenre(1L));
        verify(genreRepository, times(1)).findById(1L);
    }
}
