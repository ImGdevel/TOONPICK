package toonpick.app.webtoon.service;

import toonpick.app.webtoon.dto.AuthorDTO;
import toonpick.app.webtoon.entity.Author;
import toonpick.app.common.exception.ResourceNotFoundException;
import toonpick.app.webtoon.mapper.AuthorMapper;
import toonpick.app.webtoon.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Transactional(readOnly = true)
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorMapper::authorToAuthorDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuthorDTO getAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return authorMapper.authorToAuthorDto(author);
    }

    @Transactional(readOnly = true)
    public AuthorDTO getAuthorByName(String name) {
        Author author = authorRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with name: " + name));
        return authorMapper.authorToAuthorDto(author);
    }

    @Transactional(readOnly = true)
    public Optional<AuthorDTO> findAuthorByName(String name) {
        return authorRepository.findByName(name).map(authorMapper::authorToAuthorDto);
    }

    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        if (authorRepository.existsByName(authorDTO.getName())) {
            throw new IllegalArgumentException("Author with name " + authorDTO.getName() + " already exists.");
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
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
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
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        authorRepository.delete(author);
    }
}
