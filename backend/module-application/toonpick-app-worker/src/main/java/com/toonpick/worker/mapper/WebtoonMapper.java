package com.toonpick.worker.mapper;

import com.toonpick.worker.dto.command.AuthorRequest;
import com.toonpick.worker.dto.command.WebtoonCreateCommend;
import com.toonpick.domain.webtoon.entity.Author;
import com.toonpick.domain.webtoon.entity.Genre;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import com.toonpick.worker.domain.service.AuthorService;
import com.toonpick.worker.domain.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class WebtoonMapper {

    private final AuthorService authorService;
    private final GenreService genreService;

    public Webtoon toWebtoon(WebtoonCreateCommend request) {
        if (request == null) {
            return null;
        }

        return Webtoon.builder()
                .title(request.getTitle())
                .dayOfWeek(parseDayOfWeek(request.getDayOfWeek()))
                .thumbnailUrl(request.getThumbnailUrl())
                .ageRating(request.getAgeRating())
                .summary(request.getDescription())
                .serializationStatus(parseSerializationStatus(request.getStatus()))
                .publishStartDate(parseDate(request.getPublishStartDate()))
                .lastUpdatedDate(parseDate(request.getLastUpdatedDate()))
                .authors(mapAuthors(request.getAuthors()))
                .genres(mapGenres(request.getGenres()))
                .build();
    }

    private Set<Author> mapAuthors(List<AuthorRequest> authorDTOs) {
        if (authorDTOs == null) {
            return new HashSet<>();
        }

        return authorDTOs.stream()
                .map(authorService::findOrCreateAuthor)
                .collect(Collectors.toSet());
    }

    private Set<Genre> mapGenres(List<String> genreNames) {
        if (genreNames == null) {
            return new HashSet<>();
        }

        return genreNames.stream()
                .map(genreService::findOrCreateGenre)
                .collect(Collectors.toSet());
    }

    private DayOfWeek parseDayOfWeek(String dayOfWeek) {
        if (dayOfWeek == null) {
            return null;
        }
        try {
            return DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private SerializationStatus parseSerializationStatus(SerializationStatus status) {
        if (status == null) {
            return SerializationStatus.ONGOING;
        }
        return status;
    }

    private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return null;
        }
    }
}
