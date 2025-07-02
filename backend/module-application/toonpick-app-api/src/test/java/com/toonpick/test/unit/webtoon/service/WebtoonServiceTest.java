package com.toonpick.test.unit.webtoon.service;

import com.toonpick.common.exception.EntityNotFoundException;
import com.toonpick.common.type.ErrorCode;
import com.toonpick.domain.dto.PagedResponseDTO;
import com.toonpick.domain.webtoon.dto.WebtoonFilterDTO;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.repository.WebtoonRepository;
import com.toonpick.test.config.UnitTest;
import com.toonpick.webtoon.mapper.WebtoonMapper;
import com.toonpick.webtoon.response.WebtoonDetailsResponse;
import com.toonpick.webtoon.response.WebtoonResponse;
import com.toonpick.webtoon.service.WebtoonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
@DisplayName("WebtoonService 유닛 테스트")
class WebtoonServiceTest {

    @InjectMocks
    private WebtoonService webtoonService;

    @Mock
    private WebtoonRepository webtoonRepository;

    @Mock
    private WebtoonMapper webtoonMapper;

    @Test
    @DisplayName("존재하는 ID로 웹툰을 조회하면 WebtoonResponse를 반환한다")
    void getWebtoon_존재하는_ID_조회() {
        // given
        Long id = 1L;
        Webtoon webtoon = mock(Webtoon.class);
        WebtoonResponse response = mock(WebtoonResponse.class);
        when(webtoonRepository.findById(id)).thenReturn(Optional.of(webtoon));
        when(webtoonMapper.toWebtoonResponse(webtoon)).thenReturn(response);

        // when
        WebtoonResponse result = webtoonService.getWebtoon(id);

        // then
        assertEquals(response, result);
        verify(webtoonRepository).findById(id);
        verify(webtoonMapper).toWebtoonResponse(webtoon);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 웹툰을 조회하면 예외가 발생한다")
    void getWebtoon_존재하지_않는_ID_예외() {
        // given
        Long id = 999L;
        when(webtoonRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> webtoonService.getWebtoon(id));
        assertEquals(ErrorCode.WEBTOON_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    @DisplayName("존재하는 ID로 웹툰 상세 정보를 조회하면 WebtoonDetailsResponse를 반환한다")
    void getWebtoonDetails_존재하는_ID_조회() {
        // given
        Long id = 1L;
        Webtoon webtoon = mock(Webtoon.class);
        WebtoonDetailsResponse response = mock(WebtoonDetailsResponse.class);
        when(webtoonRepository.findById(id)).thenReturn(Optional.of(webtoon));
        when(webtoonMapper.toWebtoonDetailsResponse(webtoon)).thenReturn(response);

        // when
        WebtoonDetailsResponse result = webtoonService.getWebtoonDetails(id);

        // then
        assertEquals(response, result);
        verify(webtoonRepository).findById(id);
        verify(webtoonMapper).toWebtoonDetailsResponse(webtoon);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 웹툰 상세 정보를 조회하면 예외가 발생한다")
    void getWebtoonDetails_존재하지_않는_ID_예외() {
        // given
        Long id = 999L;
        when(webtoonRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> webtoonService.getWebtoonDetails(id));
        assertEquals(ErrorCode.WEBTOON_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    @DisplayName("필터와 페이지 옵션으로 웹툰 리스트를 조회하면 PagedResponseDTO를 반환한다")
    void getWebtoonsOptions_정상_조회() {
        // given
        WebtoonFilterDTO filter = mock(WebtoonFilterDTO.class);
        int page = 0, size = 10;
        String sortBy = "title", sortDir = "ASC";
        Webtoon webtoon = mock(Webtoon.class);
        WebtoonResponse response = mock(WebtoonResponse.class);
        List<Webtoon> webtoonList = List.of(webtoon);
        Page<Webtoon> webtoonPage = new PageImpl<>(webtoonList, PageRequest.of(page, size), 1);
        when(webtoonRepository.findWebtoonsByFilterOptions(filter, PageRequest.of(page, size, Sort.Direction.ASC, sortBy))).thenReturn(webtoonPage);
        when(webtoonMapper.toWebtoonResponse(webtoon)).thenReturn(response);

        // when
        PagedResponseDTO<WebtoonResponse> result = webtoonService.getWebtoonsOptions(filter, page, size, sortBy, sortDir);

        // then
        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(response, result.getData().get(0));
        verify(webtoonRepository).findWebtoonsByFilterOptions(filter, PageRequest.of(page, size, Sort.Direction.ASC, sortBy));
        verify(webtoonMapper).toWebtoonResponse(webtoon);
    }
} 