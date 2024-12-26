package toonpick.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.dto.AuthorDTO;
import toonpick.app.dto.GenreDTO;
import toonpick.app.dto.WebtoonUpdateRequestDTO;
import toonpick.app.service.AuthorService;
import toonpick.app.service.GenreService;
import toonpick.app.service.WebtoonService;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/webtoon-update-request")
@RequiredArgsConstructor
public class WebtoonDataUpdateController {
    private final WebtoonService webtoonService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @PostMapping("/update")
    public ResponseEntity<String> updateWebtoon(@RequestBody WebtoonUpdateRequestDTO updateRequest) {
        try {

            webtoonService.updateWebtoon(updateRequest);
            return ResponseEntity.ok("Webtoon updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating webtoon: " + e.getMessage());
        }
    }

}
