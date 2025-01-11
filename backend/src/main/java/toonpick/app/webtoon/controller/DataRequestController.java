package toonpick.app.webtoon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.webtoon.dto.AuthorDTO;
import toonpick.app.webtoon.dto.GenreDTO;
import toonpick.app.webtoon.dto.WebtoonDTO;
import toonpick.app.webtoon.dto.WebtoonRequestDTO;
import toonpick.app.webtoon.entity.enums.AgeRating;
import toonpick.app.webtoon.entity.enums.Platform;
import toonpick.app.webtoon.entity.enums.SerializationStatus;
import toonpick.app.webtoon.service.AuthorService;
import toonpick.app.webtoon.service.GenreService;
import toonpick.app.webtoon.service.WebtoonService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/webtoon-request")
public class DataRequestController {

    private final WebtoonService webtoonService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<List<WebtoonDTO>> createWebtoons(@RequestBody List<WebtoonRequestDTO> webtoonRequests) {
        List<WebtoonDTO> createdWebtoons = webtoonRequests.stream()
                .map(this::convertToWebtoonDTO)
                .map(webtoonService::createWebtoon)
                .collect(Collectors.toList());

        return ResponseEntity.ok(createdWebtoons);
    }

    private static final Map<String, DayOfWeek> DAY_OF_WEEK_MAP = Map.of(
            "일", DayOfWeek.SUNDAY,
            "월", DayOfWeek.MONDAY,
            "화", DayOfWeek.TUESDAY,
            "수", DayOfWeek.WEDNESDAY,
            "목", DayOfWeek.THURSDAY,
            "금", DayOfWeek.FRIDAY,
            "토", DayOfWeek.SATURDAY
    );

    private Platform mapToPlatform(String platform) {
        try {
            return Platform.valueOf(platform.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private AgeRating mapToAgeRating(String ageRating) {
        try {
            if (ageRating.matches("\\d+")) {
                return AgeRating.valueOf("AGE_" + ageRating);
            }
            return AgeRating.valueOf(ageRating.toUpperCase());
        } catch (IllegalArgumentException e) {
            return AgeRating.ALL;
        }
    }

    private SerializationStatus mapToSerializationStatus(String status) {
        try {
            return SerializationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private WebtoonDTO convertToWebtoonDTO(WebtoonRequestDTO request) {
        Set<AuthorDTO> authorDTOs = request.getAuthors().stream()
                .map(authorRequest -> AuthorDTO.builder()
                        .name(authorRequest.getName())
                        .role(authorRequest.getRole())
                        .link(authorRequest.getLink())
                        .build())
                .map(authorService::findOrCreateAuthor)
                .collect(Collectors.toSet());

        Set<GenreDTO> genreDTOs = request.getGenres().stream()
                .map(genreName -> GenreDTO.builder().name(genreName).build())
                .map(genreService::findOrCreateGenre)
                .collect(Collectors.toSet());

        DayOfWeek dayOfWeek = DAY_OF_WEEK_MAP.getOrDefault(request.getDay(), DayOfWeek.MONDAY);

        return WebtoonDTO.builder()
                .title(request.getTitle())
                .platform(mapToPlatform(request.getPlatform()))
                .platformId(String.valueOf(request.getUniqueId())) // uniqueId 처리
                .averageRating(0)
                .platformRating(request.getRating())
                .description(request.getStory())
                .episodeCount(request.getEpisodeCount() != 0 ? request.getEpisodeCount() : 1)
                .serializationStartDate(request.getFirstDay() != null ? request.getFirstDay() : LocalDate.now()) // firstDay 추가
                .lastUpdatedDate(request.getLastUpdateDay() != null ? request.getLastUpdateDay() : LocalDate.now()) // lastUpdateDay 추가
                .serializationStatus(mapToSerializationStatus(request.getStatus()))
                .week(dayOfWeek)
                .thumbnailUrl(request.getThumbnailUrl())
                .url(request.getUrl())
                .ageRating(mapToAgeRating(request.getAgeRating()))
                .authors(authorDTOs)
                .genres(genreDTOs)
                .build();
    }
}
