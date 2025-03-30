package com.toonpick.test.unit.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import toonpick.test.domain.webtoon.Author;
import toonpick.dto.webtoon.AuthorDTO;
import com.toonpick.exception.ResourceNotFoundException;
import com.toonpick.mapper.AuthorMapper;
import com.toonpick.repository.AuthorRepository;
import com.toonpick.service.AuthorService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        author = Author.builder()
                .uid("1234")
                .name("Test Author")
                .role("Writer")
                .link("http://example.com")
                .build();

        authorDTO = AuthorDTO.builder()
                .id(1L)
                .uid("1234")
                .name("Test Author")
                .role("Writer")
                .link("http://example.com")
                .build();
    }

    @DisplayName("모든 작가를 조회하는 단위 테스트")
    @Test
    void testGetAllAuthors() {
        // given
        when(authorRepository.findAll()).thenReturn(List.of(author));
        when(authorMapper.authorToAuthorDto(author)).thenReturn(authorDTO);

        // when
        List<AuthorDTO> result = authorService.getAllAuthors();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Author", result.get(0).getName());
        verify(authorRepository, times(1)).findAll();
        verify(authorMapper, times(1)).authorToAuthorDto(author);
    }

    @DisplayName("ID로 작가를 조회하는 단위 테스트")
    @Test
    void testGetAuthorById_Success() {
        // given
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorMapper.authorToAuthorDto(author)).thenReturn(authorDTO);

        // when
        AuthorDTO result = authorService.getAuthor(1L);

        // then
        assertNotNull(result);
        assertEquals("Test Author", result.getName());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorMapper, times(1)).authorToAuthorDto(author);
    }

    @DisplayName("ID로 작가 조회 시 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testGetAuthorById_NotFound() {
        // given
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> authorService.getAuthor(1L));
    }

    @DisplayName("이름으로 작가를 조회하는 단위 테스트")
    @Test
    void testGetAuthorByName_Success() {
        // given
        when(authorRepository.findByName("Test Author")).thenReturn(Optional.of(author));
        when(authorMapper.authorToAuthorDto(author)).thenReturn(authorDTO);

        // when
        AuthorDTO result = authorService.getAuthorByName("Test Author");

        // then
        assertNotNull(result);
        assertEquals("Test Author", result.getName());
        verify(authorRepository, times(1)).findByName("Test Author");
        verify(authorMapper, times(1)).authorToAuthorDto(author);
    }

    @DisplayName("이름으로 작가 조회 시 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testGetAuthorByName_NotFound() {
        // given
        when(authorRepository.findByName("Test Author")).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> authorService.getAuthorByName("Test Author"));
    }

    @DisplayName("작가를 생성하거나 조회하는 단위 테스트")
    @Test
    void testFindOrCreateAuthor_Success() {
        // given
        when(authorRepository.findByUid(authorDTO.getUid())).thenReturn(Optional.empty());
        when(authorMapper.authorDtoToAuthor(authorDTO)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.authorToAuthorDto(author)).thenReturn(authorDTO);

        // when
        AuthorDTO result = authorService.findOrCreateAuthorDTO(authorDTO);

        // then
        assertNotNull(result);
        assertEquals("Test Author", result.getName());
        verify(authorRepository, times(1)).findByUid(authorDTO.getUid());
        verify(authorRepository, times(1)).save(author);
        verify(authorMapper, times(1)).authorToAuthorDto(author);
    }

    @DisplayName("이미 존재하는 작가를 조회하는 단위 테스트")
    @Test
    void testFindOrCreateAuthor_AlreadyExists() {
        // given
        when(authorRepository.findByUid(authorDTO.getUid())).thenReturn(Optional.of(author));
        when(authorMapper.authorToAuthorDto(author)).thenReturn(authorDTO);

        // when
        AuthorDTO result = authorService.findOrCreateAuthorDTO(authorDTO);

        // then
        assertNotNull(result);
        assertEquals("Test Author", result.getName());
        verify(authorRepository, times(1)).findByUid(authorDTO.getUid());
        verify(authorMapper, times(1)).authorToAuthorDto(author);
    }

    @DisplayName("작가를 업데이트하는 단위 테스트")
    @Test
    void testUpdateAuthor_Success() {
        // given
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.authorToAuthorDto(author)).thenReturn(authorDTO);

        // when
        AuthorDTO result = authorService.updateAuthor(1L, authorDTO);

        // then
        assertNotNull(result);
        assertEquals("Test Author", result.getName());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(author);
        verify(authorMapper, times(1)).authorToAuthorDto(author);
    }

    @DisplayName("업데이트 시 작가가 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testUpdateAuthor_NotFound() {
        // given
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> authorService.updateAuthor(1L, authorDTO));
    }

    @DisplayName("작가를 삭제하는 단위 테스트")
    @Test
    void testDeleteAuthor_Success() {
        // given
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        // when
        authorService.deleteAuthor(1L);

        // then
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).delete(author);
    }

    @DisplayName("삭제 시 작가가 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testDeleteAuthor_NotFound() {
        // given
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> authorService.deleteAuthor(1L));
    }
}
