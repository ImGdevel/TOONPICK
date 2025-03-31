package com.toonpick.controller;


import com.toonpick.dto.WebtoonCreateRequestDTO;
import com.toonpick.dto.WebtoonRequestDTO;
import com.toonpick.dto.WebtoonResponseDTO;
import com.toonpick.service.WebtoonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Webtoon", description = "웹툰 관련 API (접근 권한 : Admin)")
@RestController
@RequestMapping("/api/admin/webtoons")
@RequiredArgsConstructor
public class AdminWebtoonController {

    private final WebtoonService webtoonService;

    private static final Logger logger = LoggerFactory.getLogger(AdminWebtoonController.class);

    // todo : Admin 사용 가능한 API
    // todo : 추후 @PreAuthorize("hasRole('ADMIN')") 어노테이션을 추가할 것

    @Operation(summary = "웹툰 업데이트", description = "등록된 웹툰 정보를 업데이트 합니다 (관리자 권한)")
    @PutMapping("/{id}")
    public ResponseEntity<WebtoonResponseDTO> updateWebtoon(
            @Parameter(description = "업데이트할 웹툰 id")
            @PathVariable Long id,
            @Parameter(description = "업데이트 정보")
            @RequestBody WebtoonRequestDTO webtoonDTO
    ) {
        WebtoonResponseDTO updatedWebtoon = webtoonService.updateWebtoon(id, webtoonDTO);
        return ResponseEntity.ok(updatedWebtoon);
    }

    @PostMapping
    public ResponseEntity<WebtoonResponseDTO> createWebtoon(@RequestBody WebtoonCreateRequestDTO webtoonDTO) {
        WebtoonResponseDTO createdWebtoon = webtoonService.createWebtoon(webtoonDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWebtoon);
    }


    @Operation(summary = "웹툰 삭제", description = "등록된 웹툰을 삭제합니다 (관리자 권한)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWebtoon(
            @Parameter(description = "삭제할 웹툰 id")
            @PathVariable Long id
    ) {
        webtoonService.deleteWebtoon(id);
        return ResponseEntity.noContent().build();
    }
}
