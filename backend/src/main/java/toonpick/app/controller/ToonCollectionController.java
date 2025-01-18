package toonpick.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.domain.toon_collection.ToonCollection;
import toonpick.app.service.ToonCollectionService;
import toonpick.app.utils.AuthenticationUtil;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class ToonCollectionController {

    private final ToonCollectionService toonCollectionService;
    private final AuthenticationUtil authenticationUtil;


    @PostMapping("/create")
    public ResponseEntity<ToonCollection> createCollection(@RequestParam String title, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        ToonCollection collection = toonCollectionService.createCollection(username, title);
        return ResponseEntity.ok(collection);
    }

    @PostMapping("/{collectionId}/webtoon/{webtoonId}")
    public ResponseEntity<Void> addWebtoon(@PathVariable Long collectionId, @PathVariable Long webtoonId) {
        toonCollectionService.addWebtoon(collectionId, webtoonId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{collectionId}/webtoon/{webtoonId}")
    public ResponseEntity<Void> removeWebtoon(@PathVariable Long collectionId, @PathVariable Long webtoonId) {
        toonCollectionService.removeWebtoon(collectionId, webtoonId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{collectionId}/webtoons")
    public ResponseEntity<Void> addMultipleWebtoons(@PathVariable Long collectionId, @RequestBody List<Long> webtoonIds) {
        toonCollectionService.addMultipleWebtoons(collectionId, webtoonIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{collectionId}/webtoons")
    public ResponseEntity<Void> removeMultipleWebtoons(@PathVariable Long collectionId, @RequestBody List<Long> webtoonIds) {
        toonCollectionService.removeMultipleWebtoons(collectionId, webtoonIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{collectionId}/webtoons/clear")
    public ResponseEntity<Void> clearAllWebtoons(@PathVariable Long collectionId) {
        toonCollectionService.clearAllWebtoons(collectionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{collectionId}")
    public ResponseEntity<Void> deleteCollection(@PathVariable Long collectionId) {
        toonCollectionService.deleteCollection(collectionId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{collectionId}/title")
    public ResponseEntity<Void> updateCollectionTitle(@PathVariable Long collectionId, @RequestParam String newTitle) {
        toonCollectionService.updateCollectionTitle(collectionId, newTitle);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ToonCollection>> getCollections(Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        List<ToonCollection> collections = toonCollectionService.getCollectionsByMember(username);
        return ResponseEntity.ok(collections);
    }
}
