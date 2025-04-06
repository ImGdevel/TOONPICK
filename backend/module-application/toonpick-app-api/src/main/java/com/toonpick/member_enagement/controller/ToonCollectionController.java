package com.toonpick.member_enagement.controller;

import com.toonpick.annotation.CurrentUser;
import com.toonpick.toon_collection.response.ToonCollectionResponseDTO;
import com.toonpick.toon_collection.service.ToonCollectionService;
import com.toonpick.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Toon Collection", description = "회원 웹툰 컬렉션 (접근 권한 : Private)")
@RestController
@RequestMapping("/api/secure/collections")
@RequiredArgsConstructor
public class ToonCollectionController {

    private final ToonCollectionService toonCollectionService;

    @Operation(summary = "웹툰 컬랙션 생성", description = "회원의 웹툰 새컬렉션을 생성합니다")
    @PostMapping("/create")
    public ResponseEntity<ToonCollectionResponseDTO> createCollection(
            @Parameter(description = "생성할 컬렉션 제목", required = true)
            @RequestParam String title,
            @CurrentUser CustomUserDetails user
    ) {
        ToonCollectionResponseDTO collection = toonCollectionService.createCollection(user.getUsername(), title);
        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "컬랙션에 웹툰 추가", description = "회원 컬렉션에 웹툰을 추가합니다")
    @PostMapping("/{collectionId}/webtoon/{webtoonId}")
    public ResponseEntity<Void> addWebtoon(
            @Parameter(description = "추가될 컬렉션 id", required = true)
            @PathVariable Long collectionId,
            @Parameter(description = "추가할 웹툰 id", required = true)
            @PathVariable Long webtoonId
    ) {
        toonCollectionService.addWebtoon(collectionId, webtoonId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "컬랙션에 웹툰 제거", description = "회원 컬렉션에 웹툰을 제거합니다")
    @DeleteMapping("/{collectionId}/webtoon/{webtoonId}")
    public ResponseEntity<Void> removeWebtoon(
            @Parameter(description = "추가될 컬렉션 id", required = true)
            @PathVariable Long collectionId,
            @Parameter(description = "추가할 웹툰 id", required = true)
            @PathVariable Long webtoonId
    ) {
        toonCollectionService.removeWebtoon(collectionId, webtoonId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "웹툰 컬랙션 제거", description = "회원 웹툰 컬렉션을 제거합니다")
    @DeleteMapping("/{collectionId}")
    public ResponseEntity<Void> deleteCollection(
            @Parameter(description = "제거할 컬렉션 id")
            @PathVariable Long collectionId
    ) {
        toonCollectionService.deleteCollection(collectionId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "웹툰 컬랙션 제목 수정", description = "회원 웹툰 컬렉션 제목을 수정합니다")
    @PatchMapping("/{collectionId}/title")
    public ResponseEntity<Void> updateCollectionTitle(
            @Parameter(description = "수정할 컬렉션")
            @PathVariable Long collectionId,
            @Parameter(description = "수정할 새제목")
            @RequestParam String newTitle
    ) {
        toonCollectionService.updateCollectionTitle(collectionId, newTitle);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "웹툰 컬랙션 리스트 조회", description = "회원 웹툰 컬렉션의 웹툰 리스트를 조회합니다")
    @GetMapping
    public ResponseEntity<List<ToonCollectionResponseDTO>> getCollections(
            @CurrentUser CustomUserDetails user
    ) {
        List<ToonCollectionResponseDTO> collections = toonCollectionService.getCollectionsByMember(user.getUsername());
        return ResponseEntity.ok(collections);
    }
}
