package com.toonpick.controller;

import com.toonpick.dto.PagedResponseDTO;
import com.toonpick.dto.WebtoonFilterDTO;
import com.toonpick.dto.WebtoonResponseDTO;
import com.toonpick.service.WebtoonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Webtoon", description = "웹툰 정보 조회 및 필터링 API (접근 권한 : Public)")
@RestController
@RequestMapping("/api/public/webtoons")
@RequiredArgsConstructor
public class PublicWebtoonController {

    private final WebtoonService webtoonService;

    @Operation(summary = "웹툰 정보 조회", description = "웹툰 ID를 통해 특정 웹툰의 정보를 조회합니다")
    @ApiResponse(responseCode = "200", description = "웹툰 정보를 성공적으로 반환")
    @GetMapping("/{id}")
    public ResponseEntity<WebtoonResponseDTO> getWebtoon(
            @Parameter(description = "웹툰 ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        WebtoonResponseDTO webtoonDTO = webtoonService.getWebtoonById(id);
        return ResponseEntity.ok(webtoonDTO);
    }

    @Operation(summary = "웹툰 필터 조회", description = "필터 옵션과 페이징 정보를 통해 웹툰 리스트를 조회합니다")
    @ApiResponse(responseCode = "200", description = "필터링된 웹툰 리스트를 성공적으로 반환")
    @GetMapping
    public ResponseEntity<PagedResponseDTO<WebtoonResponseDTO>> filterWebtoons(
            @Parameter(description = "필터 옵션 (제목, 장르 등)", required = false)
            @ModelAttribute WebtoonFilterDTO filter,
            @Parameter(description = "페이지 번호 (0부터 시작)", required = false, example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지당 항목 수", required = false, example = "30")
            @RequestParam(defaultValue = "30") int size,
            @Parameter(description = "정렬 기준 필드", required = false, example = "title")
            @RequestParam(defaultValue = "title") String sortBy,
            @Parameter(description = "정렬 방향 (asc/desc)", required = false, example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PagedResponseDTO<WebtoonResponseDTO> webtoons = webtoonService.getWebtoonsOptions(filter, page, size, sortBy, sortDir);
        return ResponseEntity.ok(webtoons);
    }
}

