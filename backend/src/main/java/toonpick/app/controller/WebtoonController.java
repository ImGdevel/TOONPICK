package toonpick.app.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import toonpick.app.dto.WebtoonDTO;
import toonpick.app.entity.Webtoon;
import toonpick.app.service.WebtoonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/webtoons")
public class WebtoonController {

    private final WebtoonService webtoonService;

    public WebtoonController(WebtoonService webtoonService) {
        this.webtoonService = webtoonService;
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWebtoon(@PathVariable Long id) {
        webtoonService.deleteWebtoon(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/author/{authorName}")
    public ResponseEntity<List<WebtoonDTO>> getWebtoonsByAuthorName(@PathVariable String authorName) {
        List<WebtoonDTO> webtoons = webtoonService.getWebtoonsByAuthorName(authorName);
        return new ResponseEntity<>(webtoons, HttpStatus.OK);
    }

    @GetMapping("/genre/{genreName}")
    public ResponseEntity<List<WebtoonDTO>> getWebtoonsByGenreName(@PathVariable String genreName) {
        List<WebtoonDTO> webtoons = webtoonService.getWebtoonsByGenreName(genreName);
        return new ResponseEntity<>(webtoons, HttpStatus.OK);
    }

    @GetMapping("/day-of-week/{dayOfWeek}")
    public ResponseEntity<List<WebtoonDTO>> getWebtoonsByDayOfWeek(@PathVariable DayOfWeek dayOfWeek) {
        List<WebtoonDTO> webtoons = webtoonService.getWebtoonsByDayOfWeek(dayOfWeek);
        return new ResponseEntity<>(webtoons, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<WebtoonDTO>> getWebtoonsByStatus(@PathVariable String status) {
        List<WebtoonDTO> webtoons = webtoonService.getWebtoonsByStatus(status);
        return new ResponseEntity<>(webtoons, HttpStatus.OK);
    }

    @GetMapping("/series/{dayOfWeek}")
    public ResponseEntity<List<WebtoonDTO>> getSeriesOfWebtoonsByDayOfWeek(@PathVariable DayOfWeek dayOfWeek) {
        List<WebtoonDTO> webtoons = webtoonService.getSeriesOfWebtoonsByDayOfWeek(dayOfWeek);
        return new ResponseEntity<>(webtoons, HttpStatus.OK);
    }

    @GetMapping("/series-all")
    public ResponseEntity<List<WebtoonDTO>> getSeriesOfWebtoons() {
        List<WebtoonDTO> webtoons = webtoonService.getWebtoonsByStatus("연재");
        return new ResponseEntity<>(webtoons, HttpStatus.OK);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<WebtoonDTO>> getCompletedWebtoons(
            @RequestParam(defaultValue = "0") int page, // 0부터 시작하도록 수정
            @RequestParam(defaultValue = "60") int size) { // 기본 size를 50으로 변경

        System.out.println("Requested Page: " + page); // 페이지 번호 확인 로그
        List<WebtoonDTO> webtoons = webtoonService.getWebtoonsByStatus("완결", page, size);
        return new ResponseEntity<>(webtoons, HttpStatus.OK);
    }

}
