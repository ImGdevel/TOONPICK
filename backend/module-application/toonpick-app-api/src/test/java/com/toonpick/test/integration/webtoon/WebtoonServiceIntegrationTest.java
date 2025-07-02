package com.toonpick.test.integration.webtoon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.domain.webtoon.dto.WebtoonFilterDTO;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.enums.AgeRating;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.test.config.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@DisplayName("Webtoon 통합 테스트")
class WebtoonServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebtoonRepository webtoonRepository;

    @BeforeEach
    void setUp() {
        webtoonRepository.deleteAll();
        Webtoon webtoon = Webtoon.builder()
            .title("테스트 웹툰")
            .externalId("EXT-1")
            .dayOfWeek(java.time.DayOfWeek.MONDAY)
            .thumbnailUrl("http://test.com/image.png")
            .ageRating(AgeRating.ALL)
            .summary("테스트 웹툰 요약")
            .serializationStatus(SerializationStatus.ONGOING)
            .publishStartDate(java.time.LocalDate.now().minusDays(10))
            .lastUpdatedDate(java.time.LocalDate.now())
            .build();
        Webtoon webtoonSaved =  webtoonRepository.save(webtoon);
    }

    @Test
    @DisplayName("웹툰 목록 조회 및 필터 적용 목록 조회")
    void getWebtoonsByFilter_기본_동작_테스트() throws Exception {
        // given
        WebtoonFilterDTO filter = new WebtoonFilterDTO();

        // when & then
        mockMvc.perform(post("/api/v1/webtoons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter))
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "title")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.page").value(0));
    }

    @Test
    @DisplayName("웹툰 목록 조회 및 필터 적용 목록 조회 - 실제 데이터 값 검증")
    void getWebtoonsByFilter_실제_값_검증() throws Exception {
        WebtoonFilterDTO filter = new WebtoonFilterDTO();

        mockMvc.perform(post("/api/v1/webtoons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter))
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "title")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("테스트 웹툰"))
                .andExpect(jsonPath("$.data[0].status").value("ONGOING"))
        ;
    }

    @Test
    @DisplayName("웹툰 상세 정보 조회")
    void getWebtoonDetails_상세_조회_기본_동작_테스트() throws Exception {
        // given
        Long webtoonId = 1L;

        // when & then
        mockMvc.perform(get("/api/v1/webtoons/detail/{id}", webtoonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(webtoonId));
    }

    @Test
    @DisplayName("웹툰 상세 정보 조회 - 실제 데이터 값 검증")
    void getWebtoonDetails_상세_조회_실제_값_검증() throws Exception {
        Webtoon webtoon = webtoonRepository.findAll().get(0);
        Long webtoonId = webtoon.getId();

        mockMvc.perform(get("/api/v1/webtoons/detail/{id}", webtoonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(webtoonId))
                .andExpect(jsonPath("$.title").value("테스트 웹툰"))
                .andExpect(jsonPath("$.summary").value("테스트 웹툰 요약"))
                .andExpect(jsonPath("$.ageRating").value("ALL"))
                .andExpect(jsonPath("$.status").value("ONGOING"))
                .andExpect(jsonPath("$.thumbnailUrl").value("http://test.com/image.png"));
    }
} 