package com.toonpick.service;

import com.toonpick.dto.request.WebtoonCreateCommand;
import com.toonpick.entity.Author;
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
    public Author findOrCreateAuthor(WebtoonCreateCommand.AuthorRequest request) {
        return authorRepository.findByUid(request.getUid())
                .orElseGet(() -> {
                    Author newAuthor = Author.builder()
                            .uid(request.getUid())
                            .name(request.getName())
                            .role(request.getRole())
                            .build();
                    return authorRepository.save(newAuthor);
                });
    }

}
