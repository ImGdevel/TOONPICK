package toonpick.app.webtoon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.webtoon.dto.WebtoonDTO;
import toonpick.app.webtoon.dto.WebtoonFilterDTO;
import toonpick.app.webtoon.entity.enums.AgeRating;
import toonpick.app.webtoon.entity.enums.Platform;
import toonpick.app.webtoon.entity.enums.SerializationStatus;
import toonpick.app.webtoon.service.WebtoonService;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/webtoons")
public class PublicWebtoonController {

    private final WebtoonService webtoonService;

    @GetMapping("/{id}")
    public ResponseEntity<WebtoonDTO> getWebtoon(@PathVariable Long id) {
        WebtoonDTO webtoonDTO = webtoonService.getWebtoon(id);
        return ResponseEntity.ok(webtoonDTO);
    }

    @GetMapping
    public ResponseEntity<List<WebtoonDTO>> filterWebtoons(
            @RequestParam(required = false) Platform platform,
            @RequestParam(required = false) SerializationStatus serializationStatus,
            @RequestParam(required = false) AgeRating ageRating,
            @RequestParam(required = false) DayOfWeek week,
            @RequestParam(required = false) Set<String> genres,
            @RequestParam(required = false) Set<String> authors,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        WebtoonFilterDTO filter = WebtoonFilterDTO.builder()
                .platform(platform)
                .serializationStatus(serializationStatus)
                .ageRating(ageRating)
                .week(week)
                .genres(genres)
                .authors(authors)
                .build();

        List<WebtoonDTO> webtoons = webtoonService.filterWebtoonsOptions(filter, page, size, sortBy, sortDir);

        //System.out.println("filter: " + platform + " State: " + serializationStatus);
        //System.out.println("page: " + page + " size: " + size + " webtoons: " + webtoons.size() + " sortBy: " + sortBy + " sortDir: " + sortDir);

        return ResponseEntity.ok(webtoons);
    }

    // todo : 추후 아래 요청들은 제거할 것

    @GetMapping("/completed")
    public ResponseEntity<List<WebtoonDTO>> getCompletedWebtoons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return filterWebtoons(null, SerializationStatus.COMPLETED, null, null, null, null, page, size, "title", "asc");
    }

    @GetMapping("/recent")
    public ResponseEntity<List<WebtoonDTO>> getRecentWebtoons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return filterWebtoons(null, SerializationStatus.ONGOING, null, null, null, null, page, size, "updatedDate", "desc");
    }

    @GetMapping("/popular")
    public ResponseEntity<List<WebtoonDTO>> getPopularWebtoons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "60") int size) {

        return filterWebtoons(null, SerializationStatus.ONGOING, null, null, null, null, page, size, "updatedDate", "desc");
    }
}
