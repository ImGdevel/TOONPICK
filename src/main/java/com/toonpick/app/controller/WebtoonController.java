package com.toonpick.app.controller;

import com.toonpick.app.dto.WebtoonDTO;
import com.toonpick.app.service.WebtoonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
