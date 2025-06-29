package com.toonpick.worker.domain.service;

import com.toonpick.domain.webtoon.entity.Author;
import com.toonpick.domain.webtoon.enums.AuthorRole;
import com.toonpick.domain.webtoon.repository.AuthorRepository;
import com.toonpick.worker.dto.command.AuthorRequest;
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

    private AuthorRole parseAuthorRole(String role) {
        if (role == null) {
            return AuthorRole.ORIGINAL;
        }
        try {
            return AuthorRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return AuthorRole.ORIGINAL;
        }
    }
}
