package toonpick.app.webtoon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.webtoon.dto.PagedResponseDTO;
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
        WebtoonDTO webtoonDTO = webtoonService.getWebtoonById(id);
        return ResponseEntity.ok(webtoonDTO);
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<WebtoonDTO>> filterWebtoons(
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

        PagedResponseDTO<WebtoonDTO> webtoons = webtoonService.getWebtoonsOptions(filter, page, size, sortBy, sortDir);



        return ResponseEntity.ok(webtoons);
    }

}
