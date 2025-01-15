package toonpick.app.controller.webtoon;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.dto.PagedResponseDTO;
import toonpick.app.dto.WebtoonDTO;
import toonpick.app.dto.WebtoonFilterDTO;
import toonpick.app.service.WebtoonService;

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
            @ModelAttribute WebtoonFilterDTO filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PagedResponseDTO<WebtoonDTO> webtoons = webtoonService.getWebtoonsOptions(filter, page, size, sortBy, sortDir);
        return ResponseEntity.ok(webtoons);
    }

}
