package com.toonpick.service;

import com.toonpick.exception.ResourceNotFoundException;
import com.toonpick.mapper.AuthorMapper;
import com.toonpick.repository.AuthorRepository;
import com.toonpick.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import com.toonpick.dto.AuthorDTO;
import com.toonpick.entity.Author;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    /**
     * 모든 작가 조회
     */
    @Transactional(readOnly = true)
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorMapper::authorToAuthorDto)
                .collect(Collectors.toList());
    }

    /**
     * id 값으로 작가 정보 조회
     */
    @Transactional(readOnly = true)
    public AuthorDTO getAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.AUTHOR_NOT_FOUND, id));
        return authorMapper.authorToAuthorDto(author);
    }

    /**
     * 작가 이름으로 작가 정보 조회
     */
    @Transactional(readOnly = true)
    public AuthorDTO getAuthorByName(String name) {
        Author author = authorRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.AUTHOR_NOT_FOUND, name));
        return authorMapper.authorToAuthorDto(author);
    }

    // todo : 아래 내용은 잘못된 설계, 응용 계층에서 분할하여 구현할 것
    /**
     * 작가 정보 가져오기, 없다면 새로 생성
     */
    @Transactional
    public Author findOrCreateAuthorEntity(AuthorDTO authorDTO) {
        return authorRepository.findByUid(authorDTO.getUid())
                .orElseGet(() -> authorRepository.save(authorMapper.authorDtoToAuthor(authorDTO)));
    }

    /**
     * 작가 정보 가져오기, 없다면 새로 생성
     */
    @Transactional
    public AuthorDTO findOrCreateAuthorDTO(AuthorDTO authorDTO) {
        Author author = findOrCreateAuthorEntity(authorDTO);
        return authorMapper.authorToAuthorDto(author);
    }

    /**
     * 작가 정보 업데이트
     */
    @Transactional
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.AUTHOR_NOT_FOUND, id));
        existingAuthor.update(
                authorDTO.getName(),
                authorDTO.getRole(),
                authorDTO.getLink()
                );
        existingAuthor = authorRepository.save(existingAuthor);
        return authorMapper.authorToAuthorDto(existingAuthor);
    }

    /**
     * 작가 제거
     */
    @Transactional
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.AUTHOR_NOT_FOUND, id));
        authorRepository.delete(author);
    }
}
