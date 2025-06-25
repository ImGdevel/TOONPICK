package com.toonpick.service;

import com.toonpick.dto.command.AuthorRequest;
import com.toonpick.entity.Author;
import com.toonpick.enums.AuthorRole;
import com.toonpick.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;

    // todo : Author 엔티티 원본을 반환하는 것이 맞을까? (WebtoonMapper 에서 사용되고 있다.)
    /**
     * Author 등록 혹은 조회
     */
    public Author findOrCreateAuthor(AuthorRequest request) {
        return authorRepository.findByUid(request.getId())
                .orElseGet(() -> {
                    Author newAuthor = Author.builder()
                            .uid(request.getId())
                            .name(request.getName())
                            .role(parseAuthorRole(request.getRole()))
                            .build();
                    return authorRepository.save(newAuthor);
                });
    }

    private com.toonpick.enums.AuthorRole parseAuthorRole(String role) {
        if (role == null) {
            return AuthorRole.ORIGINAL;
        }
        try {
            return com.toonpick.enums.AuthorRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return AuthorRole.ORIGINAL;
        }
    }
}
