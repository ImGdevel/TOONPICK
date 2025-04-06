package com.toonpick.service;

import com.toonpick.dto.WebtoonCreateRequestDTO;
import com.toonpick.entity.Author;
import com.toonpick.entity.Genre;
import com.toonpick.entity.Webtoon;
import com.toonpick.exception.ResourceAlreadyExistsException;
import com.toonpick.type.ErrorCode;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

public class WebtoonCreateService {

    /**
    새로운 웹툰 추가
     */
    @Transactional
    public WebtoonResponseDTO createWebtoon(WebtoonCreateRequestDTO createRequestDTO) {
        // 이미 등록된 웹툰인지 확인
        if (webtoonRepository.findByExternalId(createRequestDTO.getExternalId()).isPresent()) {
            throw new ResourceAlreadyExistsException(ErrorCode.WEBTOON_ALREADY_EXISTS, createRequestDTO.getTitle());
        }

        // todo : 해당 로직은 응용 모듈로 이동 시킬 것
        Set<Author> authors = createRequestDTO.getAuthors().stream()
                .map(authorService::findOrCreateAuthorEntity)
                .collect(Collectors.toSet());

        // todo : 해당 로직은 응용 모듈로 이동 시킬 것
        Set<Genre> genres = createRequestDTO.getGenres().stream()
                .map(genreService::findOrCreateGenreEntity)
                .collect(Collectors.toSet());

        Webtoon newWebtoon = Webtoon.builder()
                .externalId(createRequestDTO.getExternalId())
                .title(createRequestDTO.getTitle())
                .platform(createRequestDTO.getPlatform())
                .dayOfWeek(createRequestDTO.getDayOfWeek())
                .thumbnailUrl(createRequestDTO.getThumbnailUrl())
                .link(createRequestDTO.getLink())
                .ageRating(createRequestDTO.getAgeRating())
                .description(createRequestDTO.getDescription())
                .serializationStatus(createRequestDTO.getSerializationStatus())
                .episodeCount(createRequestDTO.getEpisodeCount())
                .platformRating(createRequestDTO.getPlatformRating())
                .publishStartDate(createRequestDTO.getPublishStartDate())
                .lastUpdatedDate(createRequestDTO.getLastUpdatedDate())
                .authors(authors)
                .genres(genres)
                .build();

        Webtoon savedWebtoon = webtoonRepository.save(newWebtoon);
        return webtoonMapper.webtoonToWebtoonResponseDto(savedWebtoon);
    }
}
