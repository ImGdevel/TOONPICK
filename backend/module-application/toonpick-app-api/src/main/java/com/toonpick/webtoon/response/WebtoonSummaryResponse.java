package com.toonpick.webtoon.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 웹툰의 요약 정보 Response DTO 객체
 * 일부 요청의 경우 웹툰의 일부 데이터만 필요한 경우를 고려한 요약 객체
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebtoonSummaryResponse {

    private Long id;

    private String title;

    private String thumbnailUrl;

    private String status;

    private String dayOfWeek;

    private Float averageRating;

    private List<PlatformResponse> platforms;

    private List<GenreResponse> genres;

    private List<AuthorResponse> authors;

    private LocalDate lastUpdateDate;
}
