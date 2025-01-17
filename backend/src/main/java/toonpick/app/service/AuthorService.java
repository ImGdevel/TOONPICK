package toonpick.app.service;

import lombok.RequiredArgsConstructor;
import toonpick.app.dto.AuthorDTO;
import toonpick.app.domain.webtoon.Author;
import toonpick.app.exception.ErrorCode;
import toonpick.app.exception.exception.ResourceAlreadyExistsException;
import toonpick.app.exception.exception.ResourceNotFoundException;
import toonpick.app.mapper.AuthorMapper;
import toonpick.app.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Transactional(readOnly = true)
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorMapper::authorToAuthorDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuthorDTO getAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.AUTHOR_NOT_FOUND, id));
        return authorMapper.authorToAuthorDto(author);
    }

    @Transactional(readOnly = true)
    public AuthorDTO getAuthorByName(String name) {
        Author author = authorRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.AUTHOR_NOT_FOUND, name));
        return authorMapper.authorToAuthorDto(author);
    }

    @Transactional(readOnly = true)
    public Optional<AuthorDTO> findAuthorByName(String name) {
        return authorRepository.findByName(name).map(authorMapper::authorToAuthorDto);
    }

    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        if (authorRepository.existsByName(authorDTO.getName())) {
            throw new ResourceAlreadyExistsException(ErrorCode.AUTHOR_ALREADY_EXISTS, authorDTO.getName());
        }

        Author author = authorMapper.authorDtoToAuthor(authorDTO);
        author = authorRepository.save(author);
        return authorMapper.authorToAuthorDto(author);
    }

    @Transactional
    public AuthorDTO findOrCreateAuthor(AuthorDTO authorDTO) {
        return authorRepository.findByName(authorDTO.getName())
                .map(authorMapper::authorToAuthorDto)
                .orElseGet(() -> {
                    Author author = authorMapper.authorDtoToAuthor(authorDTO);
                    author = authorRepository.save(author);
                    return authorMapper.authorToAuthorDto(author);
                });
    }

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

    @Transactional
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.AUTHOR_NOT_FOUND, id));
        authorRepository.delete(author);
    }
}
