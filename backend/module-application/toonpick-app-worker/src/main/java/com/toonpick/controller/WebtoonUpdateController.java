package com.toonpick.controller;

import com.toonpick.dto.WebtoonCreateRequestDTO;
import com.toonpick.dto.WebtoonResponseDTO;
import com.toonpick.service.WebtoonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/worker/webtoons/update")
@RequiredArgsConstructor
public class WebtoonUpdateController {

    private static final Logger logger = LoggerFactory.getLogger(WebtoonUpdateController.class);

    private final WebtoonService webtoonService;

    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> createWebtoons(@RequestBody @Valid List<WebtoonCreateRequestDTO> requestDTOs) {
        List<WebtoonResponseDTO> successList = new ArrayList<>();
        List<String> failedList = new ArrayList<>();

        logger.info("request size : {}", requestDTOs.size());
        for (WebtoonCreateRequestDTO dto : requestDTOs) {
            try {
                WebtoonResponseDTO responseDTO = webtoonService.createWebtoon(dto);
                successList.add(responseDTO);
            } catch (Exception e) {
                failedList.add("Failed to process webtoon: " + dto.getTitle() + " - " + e.getMessage());
            }
        }

        logger.info("success : {} , failed : {}", successList.size(), failedList.size());

        Map<String, Object> result = new HashMap<>();
        result.put("success", successList);
        result.put("failed", failedList);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(result);
    }

}
