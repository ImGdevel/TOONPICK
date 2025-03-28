package toonpick.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.service.WebtoonUpdateService;

@RestController
@RequestMapping("/api/webtoons")
@RequiredArgsConstructor
public class WebtoonUpdateController {

    private final WebtoonUpdateService webtoonUpdateService;

    @PostMapping("/update-request")
    public ResponseEntity<String> updateWebtoons() {
        int count = webtoonUpdateService.sendWebtoonsForUpdate();
        return ResponseEntity.ok("Sent " + count + " webtoons for update.");
    }
}
