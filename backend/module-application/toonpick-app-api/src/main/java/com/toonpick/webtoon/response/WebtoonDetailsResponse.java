package com.toonpick.webtoon.response;

import com.toonpick.domain.webtoon.enums.AgeRating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


/**
 * 웹툰 상세 페이지 정보 Response DTO 객체
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebtoonDetailsResponse {

    /// 기본 상세 정보
    
    private Long id;

    private String title;

    private String summary;

    private String status;

    private String dayOfWeek;

    private String thumbnailUrl;

    private boolean isAdult;

    private AgeRating ageRating;

    private List<AuthorResponse> authors;

    private List<GenreResponse> genres;

    private List<PlatformResponse> platforms;

    private int episodeCount;

    private Float averageRating;

    private LocalDate publishStartDate;

    private LocalDate lastUpdateDate;


    /// 추가 상세정보

    // todo : 추가 - 비슷한 웹툰 필드 추가
    // todo : 추가 -

    // todo : 추가 - 웹툰 분석 데이터
}
