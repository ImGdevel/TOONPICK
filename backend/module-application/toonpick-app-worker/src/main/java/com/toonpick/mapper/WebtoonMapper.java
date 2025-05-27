package com.toonpick.mapper;

import com.toonpick.dto.result.WebtoonCreateResult;
import com.toonpick.dto.command.WebtoonUpdateCommand;
import com.toonpick.entity.Author;
import com.toonpick.entity.Genre;
import com.toonpick.entity.Webtoon;

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

    public Webtoon toWebtoon(WebtoonCreateResult request) {
        if (request == null) {
            return null;
        }

        return Webtoon.builder()
                .title(request.getTitle())
                .dayOfWeek(request.getDayOfWeek())
                .thumbnailUrl(request.getThumbnailUrl())
                .ageRating(request.getAgeRating())
                .summary(request.getSummary())
                .status(request.getSerializationStatus())
                .publishStartDate(request.getPublishStartDate())
                .lastUpdatedDate(request.getLastUpdatedDate())
                .authors(mapAuthors(request.getAuthors()))
                .genres(mapGenres(request.getGenres()))
                .build();
    }


    public WebtoonUpdateCommand toWebtoonUpdateCommand(Webtoon webtoon){
        if (webtoon.getPlatforms().isEmpty()) {
            return null;
        }
        return WebtoonUpdateCommand.builder()
                .id(webtoon.getId())
                .platform(webtoon.getPlatforms().get(0).getPlatform().getName())
                .url(webtoon.getPlatforms().get(0).getLink())
                .build();
    }

    public WebtoonUpdateCommand toWebtoonEpisodeUpdateCommand(Webtoon webtoon){
        if (webtoon.getPlatforms().isEmpty()) {
            return null;
        }
        return WebtoonUpdateCommand.builder()
                .id(webtoon.getId())
                .platform(webtoon.getPlatforms().get(0).getPlatform().getName())
                .url(webtoon.getPlatforms().get(0).getLink())
                .episodeCount(webtoon.getStatistics().getEpisodeCount())
                .build();
    }


    private Set<Author> mapAuthors(Set<WebtoonCreateResult.AuthorRequest> authorDTOs) {
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
