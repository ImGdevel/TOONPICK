package toonpick.app.webtoon.controller;

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

@RestController
@RequestMapping("/api/webtoon-request")
public class DataRequestController {

    private final WebtoonService webtoonService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public DataRequestController(WebtoonService webtoonService, AuthorService authorService, GenreService genreService) {
        this.webtoonService = webtoonService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @PostMapping
    public ResponseEntity<List<WebtoonDTO>> createWebtoons(@RequestBody List<WebtoonRequestDTO> webtoonRequests) {
        List<WebtoonDTO> createdWebtoons = webtoonRequests.stream()
                .map(this::convertToWebtoonDTO)
                .map(webtoonService::createWebtoon)
                .collect(Collectors.toList());

        return ResponseEntity.ok(createdWebtoons);
    }

    // 요일 변환 맵
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
            // 숫자로 들어온 경우 처리
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
        // Authors DTO 변환
        Set<AuthorDTO> authorDTOs = request.getAuthors().stream()
                .map(authorRequest -> AuthorDTO.builder()
                        .name(authorRequest.getName())
                        .role(authorRequest.getRole())
                        .link(authorRequest.getLink())
                        .build())
                .map(authorService::findOrCreateAuthor)
                .collect(Collectors.toSet());

        // Genres DTO 변환
        Set<GenreDTO> genreDTOs = request.getGenres().stream()
                .map(genreName -> GenreDTO.builder().name(genreName).build())
                .map(genreService::findOrCreateGenre)
                .collect(Collectors.toSet());

        // DayOfWeek 변환
        DayOfWeek dayOfWeek = DAY_OF_WEEK_MAP.getOrDefault(request.getDay(), DayOfWeek.MONDAY);

        // Webtoon DTO 생성
        return WebtoonDTO.builder()
                .title(request.getTitle())
                .platform(mapToPlatform(request.getPlatform())) // 플랫폼 매핑
                .platformId(request.getUniqueId())
                .averageRating(0) // 기본 평균 평점 설정
                .platformRating(Float.parseFloat(request.getRating())) // 플랫폼 평점
                .description(request.getStory())
                .episodeCount(request.getEpisodeCount() != 0 ? request.getEpisodeCount() : 1) // 기본값 설정
                .serializationStartDate(LocalDate.now()) // 현재 날짜 사용
                .lastUpdatedDate(LocalDate.now()) // 최신 업데이트 날짜 설정
                .serializationStatus(mapToSerializationStatus(request.getStatus())) // 연재 상태 매핑
                .week(dayOfWeek)
                .thumbnailUrl(request.getThumbnailUrl())
                .url(request.getUrl())
                .ageRating(mapToAgeRating(request.getAgeRating())) // 연령대 매핑
                .authors(authorDTOs)
                .genres(genreDTOs)
                .build();
    }
}
