package toonpick.app.controller;

import toonpick.app.dto.WebtoonDTO;
import toonpick.app.service.WebtoonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

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

}
