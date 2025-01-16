package toonpick.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.dto.WebtoonDTO;
import toonpick.app.service.WebtoonService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/webtoons")
public class AdminWebtoonController {

    private final WebtoonService webtoonService;

    // todo : Admin 사용 가능한 API
    // todo : 추후 @PreAuthorize("hasRole('ADMIN')") 어노테이션을 추가할 것

    @PutMapping("/{id}")

    public ResponseEntity<WebtoonDTO> updateWebtoon(@PathVariable Long id, @RequestBody WebtoonDTO webtoonDTO) {
        WebtoonDTO updatedWebtoon = webtoonService.updateWebtoon(id, webtoonDTO);
        return ResponseEntity.ok(updatedWebtoon);
    }

    @PostMapping
    public ResponseEntity<WebtoonDTO> createWebtoon(@RequestBody WebtoonDTO webtoonDTO) {
        WebtoonDTO createdWebtoon = webtoonService.createWebtoon(webtoonDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWebtoon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWebtoon(@PathVariable Long id) {
        webtoonService.deleteWebtoon(id);
        return ResponseEntity.noContent().build();
    }
}
