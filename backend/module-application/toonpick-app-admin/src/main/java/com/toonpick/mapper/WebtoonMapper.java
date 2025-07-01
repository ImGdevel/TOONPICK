package com.toonpick.mapper;

import com.toonpick.dto.request.AuthorRequest;
import com.toonpick.dto.request.WebtoonCreateRequest;
import com.toonpick.domain.webtoon.entity.Author;
import com.toonpick.domain.webtoon.entity.Genre;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.service.AuthorService;
import com.toonpick.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WebtoonMapper {

    private final AuthorService authorService;

    private final GenreService genreService;

    public Webtoon toWebtoon(WebtoonCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Webtoon.builder()
                .title(request.getTitle())
                .dayOfWeek(request.getDayOfWeek())
                .thumbnailUrl(request.getThumbnailUrl())
                .ageRating(request.getAgeRating())
                .summary(request.getSummary())
                .serializationStatus(request.getSerializationStatus())
                .publishStartDate(request.getPublishStartDate())
                .lastUpdatedDate(request.getLastUpdatedDate())
                .authors(mapAuthors(request.getAuthors()))
                .genres(mapGenres(request.getGenres()))
                .build();
    }


    private Set<Author> mapAuthors(Set<AuthorRequest> authorDTOs) {
        if (authorDTOs == null) {
            return new HashSet<>();
        }

        return authorDTOs.stream()
                .map(authorService::findOrCreateAuthor)
                .collect(Collectors.toSet());
    }

    private Set<Genre> mapGenres(Set<String> genreNames) {
        if (genreNames == null) {
            return new HashSet<>();
        }

        return genreNames.stream()
                .map(genreService::findOrCreateGenre)
                .collect(Collectors.toSet());
    }
}
