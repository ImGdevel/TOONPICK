package toonpick.app.webtoon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toonpick.app.webtoon.dto.WebtoonDTO;
import toonpick.app.webtoon.dto.WebtoonFilterDTO;
import toonpick.app.webtoon.entity.enums.Platform;
import toonpick.app.webtoon.entity.enums.SerializationStatus;
import toonpick.app.webtoon.entity.enums.AgeRating;
import toonpick.app.webtoon.service.WebtoonService;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/webtoons")
@RequiredArgsConstructor
public class WebtoonController {

    private final WebtoonService webtoonService;

    @PostMapping
    public ResponseEntity<WebtoonDTO> createWebtoon(@RequestBody WebtoonDTO webtoonDTO) {
        WebtoonDTO createdWebtoon = webtoonService.createWebtoon(webtoonDTO);
        return new ResponseEntity<>(createdWebtoon, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebtoonDTO> getWebtoon(@PathVariable Long id) {
        WebtoonDTO webtoonDTO = webtoonService.getWebtoon(id);
        return new ResponseEntity<>(webtoonDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WebtoonDTO> updateWebtoon(@PathVariable Long id, @RequestBody WebtoonDTO webtoonDTO) {
        WebtoonDTO updatedWebtoon = webtoonService.updateWebtoon(id, webtoonDTO);
        return new ResponseEntity<>(updatedWebtoon, HttpStatus.OK);
    }

    @GetMapping("/series")
    public ResponseEntity<List<WebtoonDTO>> getSeriesOfWebtoons() {
        List<WebtoonDTO> webtoons = webtoonService.filterWebtoons(
                WebtoonFilterDTO.builder()
                        .serializationStatus(SerializationStatus.ONGOING)
                        .build()
        );
        return new ResponseEntity<>(webtoons, HttpStatus.OK);
    }

    @GetMapping("/series/{dayOfWeek}")
    public ResponseEntity<List<WebtoonDTO>> getSeriesOfWebtoonsByDayOfWeek(@PathVariable DayOfWeek dayOfWeek) {
        List<WebtoonDTO> webtoons = webtoonService.getSeriesOfWebtoonsByDayOfWeek(dayOfWeek);
        return new ResponseEntity<>(webtoons, HttpStatus.OK);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<WebtoonDTO>> getCompletedWebtoons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "60") int size) {

        List<WebtoonDTO> webtoons = webtoonService.getWebtoonsBySerializationStatus(SerializationStatus.COMPLETED, page, size);
        return new ResponseEntity<>(webtoons, HttpStatus.OK);
    }



    // 범용 필터링 메서드
    @GetMapping("/filter")
    public ResponseEntity<List<WebtoonDTO>> filterWebtoons(
            @RequestParam(required = false) Platform platform,
            @RequestParam(required = false) SerializationStatus serializationStatus,
            @RequestParam(required = false) AgeRating ageRating,
            @RequestParam(required = false) DayOfWeek week,
            @RequestParam(required = false) Set<String> genres,
            @RequestParam(required = false) Set<String> authors
    ) {
        WebtoonFilterDTO filter = WebtoonFilterDTO.builder()
                .platform(platform)
                .serializationStatus(serializationStatus)
                .ageRating(ageRating)
                .week(week)
                .genres(genres)
                .authors(authors)
                .build();

        List<WebtoonDTO> webtoons = webtoonService.filterWebtoons(filter);
        return new ResponseEntity<>(webtoons, HttpStatus.OK);
    }

    @GetMapping("/filter/options")
    public ResponseEntity<List<WebtoonDTO>> filterWebtoonsOptions(
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
        return new ResponseEntity<>(webtoons, HttpStatus.OK);
    }

}
